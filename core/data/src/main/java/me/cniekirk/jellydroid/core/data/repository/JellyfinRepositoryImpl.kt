package me.cniekirk.jellydroid.core.data.repository

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import kotlinx.coroutines.flow.Flow
import me.cniekirk.jellydroid.core.common.errors.LocalDataError
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.data.mapping.toServer
import me.cniekirk.jellydroid.core.data.mapping.toUser
import me.cniekirk.jellydroid.core.data.mapping.toUserView
import me.cniekirk.jellydroid.core.database.dao.ServerDao
import me.cniekirk.jellydroid.core.database.dao.UserDao
import me.cniekirk.jellydroid.core.database.entity.Server
import me.cniekirk.jellydroid.core.datastore.repository.AppPreferencesRepository
import me.cniekirk.jellydroid.core.model.UserView
import org.jellyfin.sdk.Jellyfin
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.exception.ApiClientException
import org.jellyfin.sdk.api.client.exception.InvalidContentException
import org.jellyfin.sdk.api.client.exception.InvalidStatusException
import org.jellyfin.sdk.api.client.exception.MissingBaseUrlException
import org.jellyfin.sdk.api.client.exception.MissingPathVariableException
import org.jellyfin.sdk.api.client.exception.SecureConnectionException
import org.jellyfin.sdk.api.client.exception.TimeoutException
import org.jellyfin.sdk.api.client.extensions.userApi
import org.jellyfin.sdk.api.client.extensions.userViewsApi
import org.jellyfin.sdk.api.sockets.exception.SocketException
import org.jellyfin.sdk.api.sockets.exception.SocketStoppedException
import org.jellyfin.sdk.discovery.RecommendedServerInfo
import org.jellyfin.sdk.discovery.RecommendedServerInfoScore
import org.jellyfin.sdk.model.api.AuthenticateUserByName
import org.jellyfin.sdk.model.api.ServerDiscoveryInfo
import timber.log.Timber
import javax.inject.Inject

class JellyfinRepositoryImpl @Inject constructor(
    private val jellyfin: Jellyfin,
    private val apiClient: ApiClient,
    private val serverDao: ServerDao,
    private val userDao: UserDao,
    private val appPreferencesRepository: AppPreferencesRepository
) : JellyfinRepository {

    override suspend fun discoverServers(): Flow<ServerDiscoveryInfo> =
        jellyfin.discovery.discoverLocalServers()

    override suspend fun connectToServer(serverDiscoveryInfo: ServerDiscoveryInfo) {
        serverDao.insertAll(serverDiscoveryInfo.toServer())
        apiClient.update(baseUrl = serverDiscoveryInfo.address)
    }

    override suspend fun connectToServer(address: String): Result<String, NetworkError> {
        return getSingleServer(address)
            .andThen { recommendedServerInfo ->
                val publicSystemInfo = recommendedServerInfo.systemInfo.getOrNull()

                val id = publicSystemInfo?.id
                val name = publicSystemInfo?.serverName

                if (id != null && name != null) {
                    serverDao.insertAll(Server(id, address, name))

                    apiClient.update(baseUrl = address)

                    Ok(name)
                } else {
                    Err(NetworkError.ServerError)
                }
            }
    }

    private suspend fun getSingleServer(address: String): Result<RecommendedServerInfo, NetworkError> {
        return safeApiCall {
            val servers = jellyfin.discovery.getRecommendedServers(address, RecommendedServerInfoScore.GOOD)
            if (servers.isNotEmpty()) {
                servers.first()
            } else {
                throw InvalidContentException()
            }
        }
    }

    override suspend fun authenticateUser(username: String, password: String): Result<Unit, NetworkError> {
        return safeApiCall {
            val authResult by apiClient.userApi.authenticateUserByName(
                data = AuthenticateUserByName(
                    username = username,
                    pw = password
                )
            )

            userDao.insertAll(authResult.toUser())

            apiClient.update(accessToken = authResult.accessToken)
        }
    }

    override suspend fun getUserViews(): Result<List<UserView>, NetworkError> {
        return safeApiCall { apiClient.userViewsApi.getUserViews() }
            .andThen { response ->
                val serverUrl = apiClient.baseUrl
                val data = response.content.items

                if (serverUrl != null) {
                    Ok(data?.map { it.toUserView(serverUrl) } ?: listOf())
                } else {
                    Err(NetworkError.Unknown)
                }
            }
    }

    override suspend fun getCurrentServerAndUser(): Result<String, LocalDataError> {
        val currentServerId = appPreferencesRepository.getCurrentServer()
        val serverWithUsers = serverDao.getAllServersWithUsers()
            .firstOrNull { it.server.serverId.equals(currentServerId, true) }

        return if (serverWithUsers != null) {
            val defaultUser = serverWithUsers.users.first()

            apiClient.update(accessToken = defaultUser.accessToken)

            Ok(defaultUser.userId)
        } else {
            Err(LocalDataError.ServerNotExists)
        }
    }

    private suspend fun <T> safeApiCall(block: suspend () -> T): Result<T, NetworkError> {
        return runCatching {
            block()
        }.mapError { throwable ->
            Timber.e(throwable)
            when (throwable) {
                is ApiClientException -> {
                    when (throwable) {
                        is SocketStoppedException, is SocketException, is SecureConnectionException, is TimeoutException -> {
                            NetworkError.ConnectionError
                        }
                        is MissingPathVariableException, is MissingBaseUrlException -> {
                            NetworkError.ClientConfigurationError
                        }
                        is InvalidStatusException, is InvalidContentException -> {
                            NetworkError.ServerError
                        }
                        else -> NetworkError.Unknown
                    }
                }
                else -> NetworkError.Unknown
            }
        }
    }
}