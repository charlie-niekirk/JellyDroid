package me.cniekirk.jellydroid.feature.mediaplayer.components

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.ui.PlayerView
import coil3.compose.LocalPlatformContext
import timber.log.Timber
import androidx.core.net.toUri

@OptIn(UnstableApi::class)
@Composable
internal fun MediaPlayer(
    mediaUrl: String,
    modifier: Modifier = Modifier,
    initialPosition: Long = 0L,
    onPositionChanged: (Long) -> Unit = {},
) {
    Timber.d("Media URL: $mediaUrl")
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

    AndroidView(
        modifier = modifier,
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
                setShowNextButton(false)
                setShowPreviousButton(false)
                setShowFastForwardButton(true)
                setShowRewindButton(true)
            }
        }
    )
}
