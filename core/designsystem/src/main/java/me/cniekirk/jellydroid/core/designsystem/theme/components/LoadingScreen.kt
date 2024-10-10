package me.cniekirk.jellydroid.core.designsystem.theme.components

import androidx.compose.animation.AnimatedContent
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

@Composable
fun LoadingScreen(modifier: Modifier = Modifier, isLoading: Boolean, content: @Composable () -> Unit) {
    AnimatedContent(isLoading, label = "Loading") { loading ->
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
            LoadingScreen(isLoading = true) {

            }
        }
    }
}