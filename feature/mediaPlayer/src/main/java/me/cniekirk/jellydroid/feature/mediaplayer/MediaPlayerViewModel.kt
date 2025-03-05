package me.cniekirk.jellydroid.feature.mediaplayer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.jellydroid.feature.mediaplayer.usecase.GetPlaybackInfoUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MediaPlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPlaybackInfoUseCase: GetPlaybackInfoUseCase
) : ViewModel(), ContainerHost<MediaPlayerState, MediaPlayerEffect> {

    private val args = savedStateHandle.toRoute<MediaPlayer>()

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
}
