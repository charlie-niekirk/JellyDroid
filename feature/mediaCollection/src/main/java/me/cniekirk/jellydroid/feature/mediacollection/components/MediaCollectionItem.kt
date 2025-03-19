package me.cniekirk.jellydroid.feature.mediacollection.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun MediaCollectionItem(
    name: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    aspectRatio: Float = 2 / 3f,
    onMediaItemClicked: () -> Unit,
) {
    val context = LocalContext.current

    val placeholderColor = MaterialTheme.colorScheme.secondary
    val placeholder = remember { ColorPainter(placeholderColor) }

    Column(
        modifier = modifier.clickable { onMediaItemClicked() },
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .clip(RoundedCornerShape(8.dp)),
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            placeholder = placeholder,
            contentScale = ContentScale.Crop,
            contentDescription = name
        )

        Text(
            modifier = Modifier.padding(top = 4.dp, start = 4.dp),
            text = name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}