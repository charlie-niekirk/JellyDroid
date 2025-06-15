package me.cniekirk.jellydroid.feature.mediaplayer

import androidx.lifecycle.ViewModel
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.jellydroid.feature.mediaplayer.usecase.GetPlaybackInfoUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel(assistedFactory = MediaPlayerViewModel.Factory::class)
class MediaPlayerViewModel @AssistedInject constructor(
    @Assisted private val args: MediaPlayer,
    private val getPlaybackInfoUseCase: GetPlaybackInfoUseCase
) : ViewModel(), ContainerHost<MediaPlayerState, MediaPlayerEffect> {

    override val container = container<MediaPlayerState, MediaPlayerEffect>(MediaPlayerState()) {
        loadMedia(args.mediaId)
    }

    private fun loadMedia(mediaId: String) = intent {
//        getMediaDetailsUseCase(mediaId).onSuccess { mediaDetailsUiModel ->
//            reduce {
//                state.copy(
//                    isLoading = false,
//                    mediaDetailsUiModel = mediaDetailsUiModel
//                )
//            }
//        }.onFailure { error ->
//            Timber.e(error.toString())
//        }

        getPlaybackInfoUseCase(mediaId).onSuccess { playbackInfo ->
            reduce {
                state.copy(
                    isLoading = false,
                    mediaStreamUrl = playbackInfo.mediaStreamUrl
                )
            }
        }.onFailure { error ->
            Timber.e(error.toString())
        }
    }

    fun saveCurrentPosition(currentPosition: Long) = intent {
        reduce {
            state.copy(
                mediaPosition = currentPosition
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(args: MediaPlayer): MediaPlayerViewModel
    }
}
