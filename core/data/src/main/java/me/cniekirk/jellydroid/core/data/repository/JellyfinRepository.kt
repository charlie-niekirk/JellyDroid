package me.cniekirk.jellydroid.core.data.repository

import kotlinx.coroutines.flow.Flow
import me.cniekirk.jellydroid.core.model.UserView
import org.jellyfin.sdk.model.api.ServerDiscoveryInfo

interface JellyfinRepository {

    suspend fun discoverServers(): Flow<ServerDiscoveryInfo>

    suspend fun connectToServer(serverDiscoveryInfo: ServerDiscoveryInfo)

    suspend fun authenticateUser(username: String, password: String)

    suspend fun getUserViews(): Result<List<UserView>>
}