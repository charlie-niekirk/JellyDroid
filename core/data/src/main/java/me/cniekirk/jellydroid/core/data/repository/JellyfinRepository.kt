package me.cniekirk.jellydroid.core.data.repository

import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.Flow
import me.cniekirk.jellydroid.core.common.errors.LocalDataError
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.model.LatestItem
import me.cniekirk.jellydroid.core.model.LatestItems
import me.cniekirk.jellydroid.core.model.ResumeItem
import me.cniekirk.jellydroid.core.model.UserView
import org.jellyfin.sdk.model.api.ServerDiscoveryInfo

interface JellyfinRepository {

    suspend fun discoverServers(): Flow<ServerDiscoveryInfo>

    suspend fun connectToServer(serverDiscoveryInfo: ServerDiscoveryInfo)

    suspend fun connectToServer(address: String): Result<String, NetworkError>

    suspend fun authenticateUser(username: String, password: String): Result<Unit, NetworkError>

    suspend fun getUserViews(): Result<List<UserView>, NetworkError>

    suspend fun getCurrentServerAndUser(): Result<String, LocalDataError>

    suspend fun getContinuePlayingItems(): Result<List<ResumeItem>, NetworkError>

    suspend fun getLatestMovies(): Result<List<LatestItem>, NetworkError>

    suspend fun getLatestShows(): Result<List<LatestItem>, NetworkError>
}