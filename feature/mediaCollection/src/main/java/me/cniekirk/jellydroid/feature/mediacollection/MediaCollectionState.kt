package me.cniekirk.jellydroid.feature.mediacollection

import me.cniekirk.jellydroid.core.domain.model.Media
import me.cniekirk.jellydroid.feature.mediacollection.model.ErrorType

data class MediaCollectionState(
    val isLoading: Boolean = true,
    val collectionItems: List<Media> = listOf(),
    val collectionId: String,
    val collectionName: String
)

sealed interface MediaCollectionEffect {

    data class ShowError(val error: ErrorType) : MediaCollectionEffect
}
