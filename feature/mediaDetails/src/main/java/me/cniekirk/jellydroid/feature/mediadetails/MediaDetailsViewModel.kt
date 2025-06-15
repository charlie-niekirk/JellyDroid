package me.cniekirk.jellydroid.feature.mediadetails

import androidx.lifecycle.ViewModel
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.jellydroid.core.domain.usecase.GetMediaDetailsUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel(assistedFactory = MediaDetailsViewModel.Factory::class)
class MediaDetailsViewModel @AssistedInject constructor(
    @Assisted private val args: MediaDetails,
    private val getMediaDetailsUseCase: GetMediaDetailsUseCase
) : ViewModel(), ContainerHost<MediaDetailsState, MediaDetailsEffect> {

    override val container = container<MediaDetailsState, MediaDetailsEffect>(MediaDetailsState(args.mediaTitle)) {
        loadMediaDetails(args.mediaId)
    }

    private fun loadMediaDetails(mediaId: String) = intent {
        getMediaDetailsUseCase(mediaId)
            .onSuccess {
                reduce {
                    state.copy(
                        isLoading = false,
                        mediaDetails = it
                    )
                }
            }
            .onFailure {
                reduce {
                    state.copy(isLoading = false)
                    // TODO: Show error
                }
            }
    }

    fun onPlayClicked() = intent {
        state.mediaDetails?.also {
            postSideEffect(
                MediaDetailsEffect.NavigateToPlayer(it.mediaId)
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(args: MediaDetails): MediaDetailsViewModel
    }
}
