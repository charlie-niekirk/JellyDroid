package me.cniekirk.jellydroid.feature.mediacollection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.core.jellydroid.domain.usecase.GetMediaUseCase
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.feature.mediacollection.model.ErrorType
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MediaCollectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMediaUseCase: GetMediaUseCase
) : ViewModel(), ContainerHost<MediaCollectionState, MediaCollectionEffect> {

    private val args = savedStateHandle.toRoute<MediaCollection>()

    override val container = container<MediaCollectionState, MediaCollectionEffect>(
        MediaCollectionState(collectionId = args.collectionId, collectionName = args.collectionName)
    ) {
        loadCollection(args.collectionId)
    }

    private fun loadCollection(collectionId: String) = intent {
        Timber.d(collectionId)
        getMediaUseCase("")
            .onSuccess { items ->
                reduce {
                    state.copy(
                        isLoading = false,
                        collectionItems = items
                    )
                }
            }
            .onFailure { error ->
                reduce {
                    state.copy(
                        isLoading = false
                    )
                }

                when (error) {
                    is NetworkError.AuthenticationError -> {
                        postSideEffect(MediaCollectionEffect.ShowError(ErrorType.AUTH_ERROR))
                    }
                    is NetworkError.ClientConfigurationError, is NetworkError.ServerError -> {
                        postSideEffect(MediaCollectionEffect.ShowError(ErrorType.SERVER_ERROR))
                    }
                    is NetworkError.ConnectionError, is NetworkError.Unknown -> {
                        postSideEffect(MediaCollectionEffect.ShowError(ErrorType.NETWORK_ERROR))
                    }
                }
            }
    }
}
