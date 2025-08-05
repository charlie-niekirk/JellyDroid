package me.cniekirk.jellydroid.core.designsystem.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import me.cniekirk.jellydroid.core.designsystem.theme.preview.CoilPreview

@Composable
fun MediaView(
    modifier: Modifier = Modifier,
    name: String,
    imageUrl: String,
    aspectRatio: Double,
    onUserViewClicked: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.clickable { onUserViewClicked() },
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio.toFloat())
                .clip(RoundedCornerShape(8.dp)),
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop
            ),
            contentScale = ContentScale.Crop,
            contentDescription = name
        )

        Text(
            modifier = Modifier.padding(top = 8.dp, start = 4.dp),
            text = name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Suppress("MagicNumber")
@PreviewLightDark
@Composable
private fun MediaViewPreview() {
    val name = "The Big Lebowski"
    val imageUrl = ""
    val aspectRatio = (3f / 2f).toDouble()

    CoilPreview {
        MediaView(
            modifier = Modifier.padding(16.dp),
            name = name,
            imageUrl = imageUrl,
            aspectRatio = aspectRatio,
            onUserViewClicked = {}
        )
    }
}
