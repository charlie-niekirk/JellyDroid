package me.cniekirk.jellydroid.feature.mediadetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.core.jellydroid.domain.usecase.GetMediaDetailsUseCase
import me.cniekirk.jellydroid.core.data.repository.JellyfinRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMediaDetailsUseCase: GetMediaDetailsUseCase
) : ViewModel(), ContainerHost<MediaDetailsState, MediaDetailsEffect> {

    private val args = savedStateHandle.toRoute<MediaDetails>()

    override val container = container<MediaDetailsState, MediaDetailsEffect>(MediaDetailsState(args.mediaTitle)) {
        loadMediaDetails(args.mediaId)
    }

    private fun loadMediaDetails(mediaId: String) = intent {
        getMediaDetailsUseCase(mediaId).onSuccess { mediaDetailsUiModel ->
            reduce {
                state.copy(
                    isLoading = false,
                    mediaDetailsUiModel = mediaDetailsUiModel
                )
            }
        }.onFailure {
            reduce {
                state.copy(isLoading = false)
                // TODO: Show error
            }
        }
    }
}