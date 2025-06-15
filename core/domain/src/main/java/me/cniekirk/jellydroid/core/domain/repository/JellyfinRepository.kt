package me.cniekirk.jellydroid.core.domain.repository

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.domain.model.ResumeItem
import me.cniekirk.jellydroid.core.domain.model.error.LocalDataError
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import me.cniekirk.jellydroid.core.domain.model.latest.LatestItem
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.MediaDetails
import me.cniekirk.jellydroid.core.domain.model.servers.ServerAndUsers
import me.cniekirk.jellydroid.core.domain.model.servers.User
import me.cniekirk.jellydroid.core.domain.model.views.UserView

@Suppress("TooManyFunctions")
interface JellyfinRepository {

    suspend fun getUserFromToken(): Result<String, NetworkError>

    suspend fun getUserById(currentServerId: String, userId: String): Result<User, NetworkError>

    suspend fun getUserViews(): Result<List<UserView>, NetworkError>

    suspend fun getServerWithUsersById(serverId: String): Result<ServerAndUsers, LocalDataError>

    suspend fun getServerBaseUrl(): Result<String, NetworkError>

    suspend fun getContinuePlayingItems(loggedInUserId: String): Result<List<ResumeItem>, NetworkError>

    suspend fun getLatestMovies(loggedInUserId: String): Result<List<LatestItem>, NetworkError>

    suspend fun getLatestShows(loggedInUserId: String): Result<List<LatestItem>, NetworkError>

    suspend fun getMediaDetails(mediaId: String, loggedInUserId: String): Result<MediaDetails, NetworkError>

    suspend fun getPlaybackInfo(
        mediaSourceId: String,
        startTimeTicks: Int = 0,
        audioStreamIndex: Int = 1,
        subtitleStreamIndex: Int = -1,
        maxStreamingBitrate: Int = 120000000,
        loggedInUserId: String
    ): Result<String, NetworkError>

    suspend fun getStreamUrl(mediaSourceId: String): Result<String, NetworkError>

    suspend fun updateClient(baseUrl: String, accessToken: String)
}
