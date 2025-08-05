package me.cniekirk.jellydroid.feature.mediadetails

import androidx.lifecycle.ViewModel
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.jellydroid.core.domain.model.FavoriteStatus
import me.cniekirk.jellydroid.core.domain.usecase.GetMediaDetailsUseCase
import me.cniekirk.jellydroid.core.domain.usecase.SetItemFavoriteStatusUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel(assistedFactory = MediaDetailsViewModel.Factory::class)
internal class MediaDetailsViewModel @AssistedInject constructor(
    @Assisted private val args: MediaDetails,
    private val getMediaDetailsUseCase: GetMediaDetailsUseCase,
    private val setItemFavoriteStatusUseCase: SetItemFavoriteStatusUseCase,
//    private val downloadMediaUseCase: DownloadMediaUseCase
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
            .onFailure { error ->
                reduce {
                    state.copy(
                        isLoading = false,
                        error = error
                    )
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

    fun favoriteToggled() = intent {
        val details = state.mediaDetails
        if (details != null) {
            if (details.isFavorite) {
                setItemFavoriteStatusUseCase(
                    details.mediaId,
                    FavoriteStatus.NOT_SET
                ).onSuccess {
                    reduce {
                        state.copy(mediaDetails = state.mediaDetails?.copy(isFavorite = false))
                    }
                }.onFailure {
                    Timber.e(message = it.toString())
                }
            } else {
                setItemFavoriteStatusUseCase(
                    details.mediaId,
                    FavoriteStatus.FAVORITE
                ).onSuccess {
                    reduce {
                        state.copy(mediaDetails = state.mediaDetails?.copy(isFavorite = true))
                    }
                }.onFailure {
                    Timber.e(message = it.toString())
                }
            }
        }
    }

    fun downloadClicked() = intent {
//        val mediaDetails = state.mediaDetails
//        if (mediaDetails != null) {
//            downloadMediaUseCase(mediaDetails.mediaId)
//                .onSuccess {
//
//                }
//                .onFailure {
//
//                }
//        }
    }

    @AssistedFactory
    interface Factory {
        fun create(args: MediaDetails): MediaDetailsViewModel
    }
}
