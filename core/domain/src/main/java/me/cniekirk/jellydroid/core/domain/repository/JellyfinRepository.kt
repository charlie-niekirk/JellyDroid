package me.cniekirk.jellydroid.core.domain.repository

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.domain.model.MediaDetails
import me.cniekirk.jellydroid.core.domain.model.ServerAndUsers
import me.cniekirk.jellydroid.core.model.LatestItem
import me.cniekirk.jellydroid.core.model.ResumeItem
import me.cniekirk.jellydroid.core.model.UserView
import me.cniekirk.jellydroid.core.model.errors.LocalDataError
import me.cniekirk.jellydroid.core.model.errors.NetworkError

@Suppress("TooManyFunctions")
interface JellyfinRepository {

    suspend fun getUserFromToken(): Result<String, NetworkError>

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
