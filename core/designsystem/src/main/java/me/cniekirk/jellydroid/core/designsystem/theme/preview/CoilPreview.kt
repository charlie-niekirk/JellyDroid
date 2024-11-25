package me.cniekirk.jellydroid.core.designsystem.theme.preview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.test.FakeImage
import me.cniekirk.jellydroid.core.designsystem.theme.JellyDroidTheme

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CoilPreview(content: @Composable () -> Unit) {
    val previewHandler = AsyncImagePreviewHandler {
        FakeImage(color = 0xFFADADAD.toInt())
    }

    JellyDroidTheme {
        Surface {
            CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
                content()
            }
        }
    }
}