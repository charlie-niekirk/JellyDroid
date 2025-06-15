package me.cniekirk.jellydroid.feature.mediacollection

import androidx.lifecycle.ViewModel
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import me.cniekirk.jellydroid.core.domain.model.views.CollectionKind
import me.cniekirk.jellydroid.core.domain.usecase.GetMediaCollectionUseCase
import me.cniekirk.jellydroid.feature.mediacollection.model.ErrorType
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel(assistedFactory = MediaCollectionViewModel.Factory::class)
class MediaCollectionViewModel @AssistedInject constructor(
    @Assisted private val args: MediaCollection,
    private val getMediaCollectionUseCase: GetMediaCollectionUseCase
) : ViewModel(), ContainerHost<MediaCollectionState, MediaCollectionEffect> {

    override val container = container<MediaCollectionState, MediaCollectionEffect>(
        MediaCollectionState(collectionId = args.collectionId, collectionName = args.collectionName)
    ) {
        loadCollection(args.collectionId, args.collectionType)
    }

    private fun loadCollection(collectionId: String, collectionType: CollectionType) = intent {
        val kind = when (collectionType) {
            CollectionType.MOVIES -> CollectionKind.MOVIES
            CollectionType.SERIES -> CollectionKind.SERIES
        }
        getMediaCollectionUseCase(collectionId, kind)
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

    @AssistedFactory
    interface Factory {
        fun create(args: MediaCollection): MediaCollectionViewModel
    }
}
