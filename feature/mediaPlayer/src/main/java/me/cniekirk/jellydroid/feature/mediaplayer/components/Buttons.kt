package me.cniekirk.jellydroid.feature.mediaplayer.components

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberNextButtonState
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import androidx.media3.ui.compose.state.rememberPreviousButtonState
import me.cniekirk.jellydroid.feature.mediaplayer.R

@OptIn(UnstableApi::class)
@Composable
internal fun PlayPauseButton(player: Player, modifier: Modifier = Modifier) {
    val state = rememberPlayPauseButtonState(player)
    val icon = if (state.showPlay) Icons.Default.PlayArrow else Icons.Default.Pause
    val contentDescription =
        if (state.showPlay) {
            stringResource(R.string.playpause_button_play)
        } else {
            stringResource(R.string.playpause_button_pause)
        }
    IconButton(onClick = state::onClick, modifier = modifier, enabled = state.isEnabled) {
        Icon(icon, contentDescription = contentDescription, modifier = modifier)
    }
}

@OptIn(UnstableApi::class)
@Composable
internal fun NextButton(player: Player, modifier: Modifier = Modifier) {
    val state = rememberNextButtonState(player)
    IconButton(onClick = state::onClick, modifier = modifier, enabled = state.isEnabled) {
        Icon(
            Icons.Default.SkipNext,
            contentDescription = stringResource(R.string.next_button),
            modifier = modifier,
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
internal fun PreviousButton(player: Player, modifier: Modifier = Modifier) {
    val state = rememberPreviousButtonState(player)
    IconButton(onClick = state::onClick, modifier = modifier, enabled = state.isEnabled) {
        Icon(
            Icons.Default.SkipPrevious,
            contentDescription = stringResource(R.string.previous_button),
            modifier = modifier,
        )
    }
}

@Composable
internal fun MinimalControls(player: Player, modifier: Modifier = Modifier) {
    // val graySemiTransparentBackground = Color.Gray.copy(alpha = 0.1f)
    val modifierForIconButton =
        modifier.size(48.dp) // .background(graySemiTransparentBackground, CircleShape)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PreviousButton(player, modifierForIconButton)
        PlayPauseButton(player, modifierForIconButton)
        NextButton(player, modifierForIconButton)
    }
}
