package me.cniekirk.jellydroid.core.designsystem.theme.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import me.cniekirk.jellydroid.core.designsystem.theme.JellyDroidTheme

const val IN_DURATION = 220
const val OUT_DURATION = 90

@Composable
fun LoadableScreen(modifier: Modifier = Modifier, isLoading: Boolean, content: @Composable () -> Unit) {
    AnimatedContent(
        targetState = isLoading,
        transitionSpec = {
            fadeIn(animationSpec = tween(IN_DURATION, delayMillis = OUT_DURATION))
                .togetherWith(fadeOut(animationSpec = tween(OUT_DURATION)))
        },
        label = "Loading"
    ) { loading ->
        if (loading) {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            content()
        }
    }
}

@PreviewLightDark
@Composable
private fun LoadingScreenPreview() {
    JellyDroidTheme {
        Surface {
            LoadableScreen(isLoading = true, content = {})
        }
    }
}
