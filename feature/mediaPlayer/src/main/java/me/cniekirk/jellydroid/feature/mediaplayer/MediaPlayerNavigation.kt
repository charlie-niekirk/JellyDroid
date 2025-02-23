package me.cniekirk.jellydroid.feature.mediaplayer

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultEnter
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultPopExit

@Serializable
data class MediaPlayer(
    val mediaId: String
)

fun NavGraphBuilder.mediaPlayer(onBackClicked: () -> Unit) {
    composable<MediaPlayer>(
        enterTransition = { activityDefaultEnter() },
        popExitTransition = { activityDefaultPopExit() }
    ) {
        val viewModel = hiltViewModel<MediaPlayerViewModel>()
        MediaPlayerScreen(
            viewModel = viewModel,
            onBackClicked = { onBackClicked() }
        )
    }
}