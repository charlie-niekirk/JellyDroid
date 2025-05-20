package me.cniekirk.jellydroid.feature.home.mobile.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.jellydroid.core.designsystem.theme.preview.CoilPreview
import me.cniekirk.jellydroid.core.domain.model.ResumeItem
import me.cniekirk.jellydroid.feature.home.R

@Composable
internal fun ResumeItems(
    modifier: Modifier = Modifier,
    resumeItems: ImmutableList<ResumeItem>,
    onResumeItemClicked: (String) -> Unit
) {
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
                    resumeItem = resumeItem,
                    onResumeItemClicked = { onResumeItemClicked(it) }
                )
            }
        }
    }
}

@Composable
internal fun ResumePlayingItem(
    modifier: Modifier = Modifier,
    resumeItem: ResumeItem,
    onResumeItemClicked: (String) -> Unit
) {
    val context = LocalContext.current

    val placeholderColor = MaterialTheme.colorScheme.secondary
    val placeholder = remember { ColorPainter(placeholderColor) }

    Column(
        modifier = modifier.clickable { onResumeItemClicked(resumeItem.id) }
    ) {
        Box {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(resumeItem.aspectRatio.toFloat())
                    .clip(RoundedCornerShape(8.dp)),
                model = ImageRequest.Builder(context)
                    .data(resumeItem.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = placeholder,
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
                val percentWidth = canvasWidth * (resumeItem.playedPercentage)

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

@PreviewLightDark
@Composable
private fun ResumeItemPreview() {
    CoilPreview {
        ResumePlayingItem(
            modifier = Modifier.padding(16.dp),
            resumeItem = ResumeItem(
                id = "0",
                name = "Inception",
                imageUrl = "",
                aspectRatio = (3 / 2f).toDouble(),
                playedPercentage = 55f
            ),
            onResumeItemClicked = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun ResumeItemsPreview() {
    CoilPreview {
        val items = persistentListOf(
            ResumeItem(
                id = "0",
                name = "Inception",
                imageUrl = "",
                aspectRatio = (3 / 2f).toDouble(),
                playedPercentage = 55f
            ),
            ResumeItem(
                id = "0",
                name = "Inception",
                imageUrl = "",
                aspectRatio = (3 / 2f).toDouble(),
                playedPercentage = 55f
            ),
            ResumeItem(
                id = "0",
                name = "Inception",
                imageUrl = "",
                aspectRatio = (3 / 2f).toDouble(),
                playedPercentage = 55f
            )
        )

        ResumeItems(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            resumeItems = items,
            onResumeItemClicked = {}
        )
    }
}
