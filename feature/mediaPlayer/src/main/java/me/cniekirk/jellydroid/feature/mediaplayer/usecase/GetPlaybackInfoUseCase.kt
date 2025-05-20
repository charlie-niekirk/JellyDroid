package me.cniekirk.jellydroid.feature.mediaplayer.usecase

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError

data class PlaybackInfo(
    val mediaName: String,
    val mediaStreamUrl: String
)

interface GetPlaybackInfoUseCase {

    suspend operator fun invoke(mediaId: String): Result<PlaybackInfo, NetworkError>
}
