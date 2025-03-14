package me.cniekirk.jellydroid.feature.mediacollection

data class MediaCollectionState(
    val isLoading: Boolean = true,
    val collectionName: String,
    val collectionItems: List<String>
)

sealed interface MediaCollectionEffect {

    data class ShowError(val message: String) : MediaCollectionEffect
}
