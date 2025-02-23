package me.cniekirk.jellydroid.feature.mediaplayer.components

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
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

@OptIn(UnstableApi::class)
@Composable
internal fun MediaPlayer(
    mediaUrl: String,
    modifier: Modifier = Modifier
) {
    Timber.d("Media URL: $mediaUrl")
    val context = LocalPlatformContext.current

    val httpDataSourceFactory: HttpDataSource.Factory =
        DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(false)
    val hlsMediaSource = HlsMediaSource.Factory(httpDataSourceFactory).createMediaSource(MediaItem.fromUri(mediaUrl))

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaSource(hlsMediaSource)
            playWhenReady = true
            addAnalyticsListener(EventLogger())
            prepare()
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        exoPlayer.currentPosition
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
            }
        }
    )
}