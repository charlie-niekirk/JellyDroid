package me.cniekirk.jellydroid.feature.mediadetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import kotlinx.serialization.Serializable

fun EntryProviderBuilder<*>.mediaDetails(
    onPlayClicked: (String) -> Unit,
    onBackClicked: () -> Unit
) {
    entry<MediaDetails> { key ->
        val playClicked by rememberUpdatedState(onPlayClicked)
        val backClicked by rememberUpdatedState(onBackClicked)

        val viewModel = hiltViewModel<MediaDetailsViewModel, MediaDetailsViewModel.Factory>(
            creationCallback = { factory -> factory.create(key) }
        )
        MediaDetailsScreen(
            viewModel = viewModel,
            onPlayClicked = { playClicked(it) },
            onBackClicked = { backClicked() }
        )
    }
}

@Serializable
data class MediaDetails(
    val mediaId: String,
    val mediaTitle: String
) : NavKey
