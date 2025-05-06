package me.cniekirk.jellydroid.feature.mediaplayer.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.coroutineBinding
import me.cniekirk.jellydroid.core.model.errors.NetworkError
import me.cniekirk.jellydroid.core.domain.repository.AppPreferencesRepository
import me.cniekirk.jellydroid.core.domain.repository.JellyfinRepository
import timber.log.Timber
import javax.inject.Inject

class GetPlaybackInfoUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository,
    private val jellyfinRepository: JellyfinRepository,
) : GetPlaybackInfoUseCase {

    override suspend fun invoke(mediaId: String): Result<PlaybackInfo, NetworkError> = coroutineBinding {
        val userId = appPreferencesRepository.getLoggedInUser()
//        val mediaDetails = getMediaDetailsUseCase(mediaId).bind()
        val playbackInfo = jellyfinRepository.getPlaybackInfo(mediaSourceId = mediaId, loggedInUserId = userId).bind()
//        val transcodingUrl = jellyfinRepository.getStreamUrl(mediaId).bind()

        Timber.d("Playback Info: $playbackInfo")
        PlaybackInfo(mediaName = "", playbackInfo)
    }
}
