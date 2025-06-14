package me.cniekirk.jellydroid.feature.mediaplayer

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import kotlinx.serialization.Serializable

fun EntryProviderBuilder<NavKey>.mediaPlayer() {
    entry<MediaPlayer> { key ->
        val viewModel = hiltViewModel<MediaPlayerViewModel, MediaPlayerViewModel.Factory>(
            creationCallback = { factory -> factory.create(key) }
        )

        MediaPlayerScreen(
            viewModel = viewModel,
//            onBackClicked = { onBackClicked() }
        )
    }
}

@Serializable
data class MediaPlayer(
    val mediaId: String
) : NavKey
