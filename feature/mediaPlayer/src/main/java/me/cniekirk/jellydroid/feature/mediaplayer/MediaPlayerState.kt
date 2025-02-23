package me.cniekirk.jellydroid.feature.mediaplayer

data class MediaPlayerState(
    val isLoading: Boolean = true,
    val mediaStreamUrl: String? = null,
)

sealed interface MediaPlayerEffect {

    data object ShowError : MediaPlayerEffect
}