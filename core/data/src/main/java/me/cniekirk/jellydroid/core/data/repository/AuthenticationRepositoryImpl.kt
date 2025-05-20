package me.cniekirk.jellydroid.core.data.repository

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import me.cniekirk.jellydroid.core.data.mapping.toUserDto
import me.cniekirk.jellydroid.core.data.safeApiCall
import me.cniekirk.jellydroid.core.database.dao.ServerDao
import me.cniekirk.jellydroid.core.database.dao.UserDao
import me.cniekirk.jellydroid.core.database.entity.Server
import me.cniekirk.jellydroid.core.domain.repository.AppPreferencesRepository
import me.cniekirk.jellydroid.core.domain.repository.AuthenticationRepository
import org.jellyfin.sdk.Jellyfin
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.exception.InvalidContentException
import org.jellyfin.sdk.api.client.extensions.userApi
import org.jellyfin.sdk.discovery.RecommendedServerInfo
import org.jellyfin.sdk.discovery.RecommendedServerInfoScore
import org.jellyfin.sdk.model.api.AuthenticateUserByName
import javax.inject.Inject

internal class AuthenticationRepositoryImpl @Inject constructor(
    private val jellyfin: Jellyfin,
    private val apiClient: ApiClient,
    private val serverDao: ServerDao,
    private val userDao: UserDao,
    private val appPreferencesRepository: AppPreferencesRepository
) : AuthenticationRepository {

    override suspend fun connectToServer(address: String): Result<String, NetworkError> {
        return getSingleServer(address)
            .andThen { recommendedServerInfo ->
                val publicSystemInfo = recommendedServerInfo.systemInfo.getOrNull()

                val id = publicSystemInfo?.id
                val name = publicSystemInfo?.serverName

                if (id != null && name != null) {
                    serverDao.insertAll(Server(id, address, name))
                    appPreferencesRepository.setCurrentServer(id)

                    apiClient.update(baseUrl = address)

                    Ok(name)
                } else {
                    Err(NetworkError.ServerError)
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

            val user = authResult.toUserDto()

            userDao.insertAll(user)
            appPreferencesRepository.setLoggedInUser(user.userId)
            apiClient.update(accessToken = authResult.accessToken)
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
}
