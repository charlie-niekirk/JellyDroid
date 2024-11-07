package me.cniekirk.jellydroid.feature.home.mobile.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.collections.immutable.ImmutableList
import me.cniekirk.jellydroid.core.model.ResumeItem
import me.cniekirk.jellydroid.feature.home.R

@Composable
fun ResumeItems(modifier: Modifier = Modifier, resumeItems: ImmutableList<ResumeItem>) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.resume_title),
            style = MaterialTheme.typography.titleMedium
        )

        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(resumeItems) { resumeItem ->
                ResumePlayingItem(
                    modifier = Modifier.width(212.dp),
                    resumeItem = resumeItem
                )
            }
        }
    }
}

@Composable
fun ResumePlayingItem(modifier: Modifier = Modifier, resumeItem: ResumeItem) {
    val context = LocalContext.current

    Column(
        modifier = modifier
    ) {
        Box {
            Image(
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(resumeItem.aspectRatio.toFloat())
                    .clip(RoundedCornerShape(8.dp)),
                painter =  rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(resumeItem.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop
                ),
                contentScale = ContentScale.Crop,
                contentDescription = resumeItem.name,
            )

            val primaryColor = MaterialTheme.colorScheme.primary

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            ) {
                val canvasWidth = size.width
                val percentWidth = canvasWidth * (resumeItem.playedPercentage / 100)

                drawLine(
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = percentWidth, y = 0f),
                    color = primaryColor,
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        Text(
            modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp),
            text = resumeItem.name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}