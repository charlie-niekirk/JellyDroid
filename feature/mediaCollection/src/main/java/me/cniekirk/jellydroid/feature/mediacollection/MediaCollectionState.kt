package me.cniekirk.jellydroid.feature.mediacollection

data class MediaCollectionState(
    val isLoading: Boolean = true,
    val collectionName: String,
//    val media: List<>
)

sealed interface MediaCollectionEffect {


}