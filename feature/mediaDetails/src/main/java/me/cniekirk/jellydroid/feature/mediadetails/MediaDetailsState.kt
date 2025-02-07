package me.cniekirk.jellydroid.feature.mediadetails

import me.cniekirk.core.jellydroid.domain.model.MediaDetailsUiModel

data class MediaDetailsState(
    val isLoading: Boolean = true,
    val mediaDetailsUiModel: MediaDetailsUiModel? = null
)

sealed interface MediaDetailsEffect {

    data class NavigateToPlayer(val mediaId: String) : MediaDetailsEffect
}