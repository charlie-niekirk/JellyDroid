package me.cniekirk.jellydroid.feature.mediadetails

import me.cniekirk.jellydroid.core.domain.model.MediaDetailsUiModel

data class MediaDetailsState(
    val mediaTitle: String,
    val isLoading: Boolean = true,
    val mediaDetailsUiModel: MediaDetailsUiModel? = null
)

sealed interface MediaDetailsEffect {

    data class NavigateToPlayer(val mediaId: String) : MediaDetailsEffect
}
