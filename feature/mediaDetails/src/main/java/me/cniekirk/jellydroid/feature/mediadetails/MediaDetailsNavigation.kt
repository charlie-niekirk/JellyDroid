package me.cniekirk.jellydroid.feature.mediadetails

import androidx.compose.animation.togetherWith
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.core.designsystem.theme.enterAnimation
import me.cniekirk.jellydroid.core.designsystem.theme.exitAnimation
import me.cniekirk.jellydroid.core.designsystem.theme.popEnterAnimation
import me.cniekirk.jellydroid.core.designsystem.theme.popExitAnimation

fun EntryProviderBuilder<*>.mediaDetails(
    onPlayClicked: (String) -> Unit,
    onBackClicked: () -> Unit
) {
    entry<MediaDetails>(
        metadata = NavDisplay.transitionSpec {
            enterAnimation() togetherWith exitAnimation()
        } + NavDisplay.popTransitionSpec {
            popEnterAnimation() togetherWith popExitAnimation()
        } + NavDisplay.predictivePopTransitionSpec {
            popEnterAnimation() togetherWith popExitAnimation()
        }
    ) { key ->
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
