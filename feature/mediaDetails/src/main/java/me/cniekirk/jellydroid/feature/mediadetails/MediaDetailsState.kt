package me.cniekirk.jellydroid.feature.mediadetails

data class MediaDetailsState(
    val isLoading: Boolean = true,
    val title: String = "",
    val imageUrl: String =  "",
    val codecs: List<String> = listOf(),
    val rating: String = "",
    val trailerUrl: String = ""
)

sealed interface MediaDetailsEffect {

    data class NavigateToPlayer(val mediaId: String) : MediaDetailsEffect
}