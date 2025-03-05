package me.cniekirk.jellydroid.feature.mediadetails

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.core.designsystem.theme.enterAnimation
import me.cniekirk.jellydroid.core.designsystem.theme.popExitAnimation

fun NavGraphBuilder.mediaDetails(
    onPlayClicked: (String) -> Unit,
    onBackClicked: () -> Unit
) {
    composable<MediaDetails>(
        enterTransition = { enterAnimation() },
        popExitTransition = { popExitAnimation() }
    ) {
        val viewModel = hiltViewModel<MediaDetailsViewModel>()
        MediaDetailsScreen(
            viewModel = viewModel,
            onPlayClicked = { onPlayClicked(it) },
            onBackClicked = { onBackClicked() }
        )
    }
}

@Serializable
data class MediaDetails(
    val mediaId: String,
    val mediaTitle: String
)
