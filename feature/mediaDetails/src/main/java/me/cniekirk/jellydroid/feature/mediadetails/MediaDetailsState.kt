package me.cniekirk.jellydroid.feature.mediadetails

import me.cniekirk.jellydroid.core.domain.model.MediaDetails

data class MediaDetailsState(
    val mediaTitle: String,
    val isLoading: Boolean = true,
    val mediaDetails: MediaDetails? = null
)

sealed interface MediaDetailsEffect {

    data class NavigateToPlayer(val mediaId: String) : MediaDetailsEffect
}
