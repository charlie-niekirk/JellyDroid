package me.cniekirk.jellydroid.feature.mediaplayer.components

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import androidx.media3.ui.compose.state.rememberPresentationState
import coil3.compose.LocalPlatformContext

@OptIn(UnstableApi::class)
@Composable
internal fun MediaPlayer(
    mediaUrl: String,
    modifier: Modifier = Modifier,
    initialPosition: Long = 0L,
    onPositionChanged: (Long) -> Unit = {},
) {
    val context = LocalPlatformContext.current

    val httpDataSourceFactory: HttpDataSource.Factory = remember {
        DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
    }
    val hlsMediaSource = remember(mediaUrl) {
        HlsMediaSource.Factory(httpDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(mediaUrl.toUri()))
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            addAnalyticsListener(EventLogger())
        }
    }

    LaunchedEffect(hlsMediaSource, initialPosition) {
        exoPlayer.setMediaSource(hlsMediaSource)
        exoPlayer.prepare()
        exoPlayer.seekTo(initialPosition)
        exoPlayer.playWhenReady = true
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            onPositionChanged(exoPlayer.currentPosition)
            exoPlayer.release()
        }
    }

    var showControls by remember { mutableStateOf(true) }

    val presentationState = rememberPresentationState(exoPlayer)
    val scaledModifier = Modifier.resizeWithContentScale(ContentScale.FillWidth, presentationState.videoSizeDp)

    Box(modifier) {
        PlayerSurface(
            player = exoPlayer,
            surfaceType = SURFACE_TYPE_SURFACE_VIEW,
            modifier = scaledModifier.clickable { showControls = !showControls },
        )

        if (presentationState.coverSurface) {
            Box(Modifier.matchParentSize().background(Color.Black))
        }

        if (showControls) {
            MinimalControls(exoPlayer, Modifier.align(Alignment.Center))
        }
    }
}
