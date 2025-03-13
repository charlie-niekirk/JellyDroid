package me.cniekirk.jellydroid.core.data.repository

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.common.errors.LocalDataError
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.model.LatestItem
import me.cniekirk.jellydroid.core.model.ResumeItem
import me.cniekirk.jellydroid.core.model.UserView
import org.jellyfin.sdk.model.api.BaseItemDto

@Suppress("TooManyFunctions")
interface JellyfinRepository {

    suspend fun getUserViews(): Result<List<UserView>, NetworkError>

    suspend fun getCurrentServerAndUser(): Result<String, LocalDataError>

    suspend fun getServerBaseUrl(): Result<String, NetworkError>

    suspend fun getContinuePlayingItems(): Result<List<ResumeItem>, NetworkError>

    suspend fun getLatestMovies(): Result<List<LatestItem>, NetworkError>

    suspend fun getLatestShows(): Result<List<LatestItem>, NetworkError>

    suspend fun getMediaDetails(mediaId: String): Result<BaseItemDto, NetworkError>

    suspend fun getMediaCollection(collectionId: String): Result<List<BaseItemDto>, NetworkError>

    suspend fun getPlaybackInfo(
        mediaSourceId: String,
        startTimeTicks: Int = 0,
        audioStreamIndex: Int = 1,
        subtitleStreamIndex: Int = -1,
        maxStreamingBitrate: Int = 120000000
    ): Result<String, NetworkError>

    suspend fun getStreamUrl(mediaSourceId: String): Result<String, NetworkError>
}
