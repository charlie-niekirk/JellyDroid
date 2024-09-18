package me.cniekirk.jellydroid.core.data.repository

import kotlinx.coroutines.flow.Flow
import me.cniekirk.jellydroid.core.data.mapping.toServer
import me.cniekirk.jellydroid.core.data.mapping.toUser
import me.cniekirk.jellydroid.core.data.mapping.toUserView
import me.cniekirk.jellydroid.core.database.dao.ServerDao
import me.cniekirk.jellydroid.core.database.dao.UserDao
import me.cniekirk.jellydroid.core.model.UserView
import org.jellyfin.sdk.Jellyfin
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.exception.ApiClientException
import org.jellyfin.sdk.api.client.extensions.dashboardApi
import org.jellyfin.sdk.api.client.extensions.systemApi
import org.jellyfin.sdk.api.client.extensions.userApi
import org.jellyfin.sdk.api.client.extensions.userLibraryApi
import org.jellyfin.sdk.api.client.extensions.userViewsApi
import org.jellyfin.sdk.model.api.AuthenticateUserByName
import org.jellyfin.sdk.model.api.ConfigurationPageInfo
import org.jellyfin.sdk.model.api.ServerDiscoveryInfo
import timber.log.Timber
import javax.inject.Inject

class JellyfinRepositoryImpl @Inject constructor(
    private val jellyfin: Jellyfin,
    private val apiClient: ApiClient,
    private val serverDao: ServerDao,
    private val userDao: UserDao
) : JellyfinRepository {

    override suspend fun discoverServers(): Flow<ServerDiscoveryInfo> =
        jellyfin.discovery.discoverLocalServers()

    override suspend fun connectToServer(serverDiscoveryInfo: ServerDiscoveryInfo) {
        serverDao.insertAll(serverDiscoveryInfo.toServer())
        apiClient.update(baseUrl = serverDiscoveryInfo.address)
    }

    override suspend fun authenticateUser(username: String, password: String) {
        try {
            val authResult by apiClient.userApi.authenticateUserByName(
                data = AuthenticateUserByName(
                    username = username,
                    pw = password
                )
            )

            userDao.insertAll(authResult.toUser())

            apiClient.update(accessToken = authResult.accessToken)
        } catch (apiClientException: ApiClientException) {
            Timber.e(apiClientException)
        }
    }

    override suspend fun getUserViews(): Result<List<UserView>> {
        return safeApiCall {
            val response = apiClient.userViewsApi.getUserViews().content.items ?: emptyList()
            response.map { it.toUserView(apiClient.baseUrl ?: "") }
        }
    }

    private inline fun <T> safeApiCall(block: () -> T, ): Result<T> {
        return try {
            Result.success(block())
        } catch (apiClientException: ApiClientException) {
            Timber.e(apiClientException)
            Result.failure(apiClientException)
        }
    }
}