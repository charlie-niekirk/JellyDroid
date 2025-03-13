package me.cniekirk.jellydroid.core.data.repository

import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.Flow
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import org.jellyfin.sdk.model.api.ServerDiscoveryInfo

interface AuthenticationRepository {

    suspend fun discoverServers(): Flow<ServerDiscoveryInfo>

    suspend fun connectToServer(serverDiscoveryInfo: ServerDiscoveryInfo)

    suspend fun connectToServer(address: String): Result<String, NetworkError>

    suspend fun authenticateUser(username: String, password: String): Result<Unit, NetworkError>
}
