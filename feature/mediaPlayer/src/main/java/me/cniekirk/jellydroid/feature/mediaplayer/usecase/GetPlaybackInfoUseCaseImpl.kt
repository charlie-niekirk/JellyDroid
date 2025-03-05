package me.cniekirk.jellydroid.feature.mediaplayer.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.coroutineBinding
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.data.repository.JellyfinRepository
import timber.log.Timber
import javax.inject.Inject

class GetPlaybackInfoUseCaseImpl @Inject constructor(
    private val jellyfinRepository: JellyfinRepository,
//    private val getMediaDetailsUseCase: GetMediaDetailsUseCase
) : GetPlaybackInfoUseCase {

    override suspend fun invoke(mediaId: String): Result<PlaybackInfo, NetworkError> = coroutineBinding {
//        val mediaDetails = getMediaDetailsUseCase(mediaId).bind()
        val playbackInfo = jellyfinRepository.getPlaybackInfo(mediaId).bind()
//        val transcodingUrl = jellyfinRepository.getStreamUrl(mediaId).bind()

        Timber.d("Playback Info: $playbackInfo")
        PlaybackInfo(mediaName = "", playbackInfo)
    }
}
