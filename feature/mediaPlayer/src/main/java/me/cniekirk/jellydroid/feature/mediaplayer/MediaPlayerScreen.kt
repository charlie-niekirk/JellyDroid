package me.cniekirk.jellydroid.feature.mediaplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import me.cniekirk.jellydroid.core.designsystem.theme.components.LoadableScreen
import me.cniekirk.jellydroid.feature.mediaplayer.components.MediaPlayer
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun MediaPlayerScreen(
    viewModel: MediaPlayerViewModel,
//    onBackClicked: () -> Unit
) {
    val state = viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            else -> {}
        }
    }

    MediaPlayerContent(
        state = state.value,
        saveCurrentPosition = viewModel::saveCurrentPosition,
//        onBackClicked = { onBackClicked() }
    )
}

@Composable
private fun MediaPlayerContent(
    state: MediaPlayerState,
    saveCurrentPosition: (Long) -> Unit,
//    onBackClicked: () -> Unit
) {
    LoadableScreen(isLoading = state.isLoading) {
        val mediaUrl = state.mediaStreamUrl
        if (mediaUrl != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black)
            ) {
                MediaPlayer(
                    modifier = Modifier.fillMaxSize(),
                    mediaUrl = mediaUrl,
                    initialPosition = state.mediaPosition ?: 0L,
                    onPositionChanged = { saveCurrentPosition(it) }
                )
            }
        }
    }
}
