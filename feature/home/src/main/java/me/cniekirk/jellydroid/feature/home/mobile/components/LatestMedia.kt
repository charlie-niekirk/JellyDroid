package me.cniekirk.jellydroid.feature.home.mobile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.collections.immutable.ImmutableList
import me.cniekirk.jellydroid.core.model.LatestItem
import me.cniekirk.jellydroid.feature.home.R

@Composable
fun LatestMovies(modifier: Modifier = Modifier, latestMovies: ImmutableList<LatestItem>) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.latest_movies_title),
            style = MaterialTheme.typography.titleMedium
        )

        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(latestMovies) { latestMovie ->
                LatestMedia(
                    modifier = Modifier
                        .height(212.dp)
                        .width(IntrinsicSize.Min),
                    latestItem = latestMovie
                )
            }
        }
    }
}

@Composable
fun LatestShows(modifier: Modifier = Modifier, latestShows: ImmutableList<LatestItem>) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.latest_shows_title),
            style = MaterialTheme.typography.titleMedium
        )

        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(latestShows) { latestShow ->
                LatestMedia(
                    modifier = Modifier
                        .height(212.dp)
                        .width(IntrinsicSize.Min),
                    latestItem = latestShow
                )
            }
        }
    }
}

@Composable
fun LatestMedia(modifier: Modifier = Modifier, latestItem: LatestItem) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
    ) {
        Image(
            modifier = modifier
                .weight(1f)
                .aspectRatio(2 / 3f)
                .clip(RoundedCornerShape(8.dp)),
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(latestItem.imageUrl)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop
            ),
            contentScale = ContentScale.Crop,
            contentDescription = latestItem.name
        )

        Text(
            modifier = Modifier.padding(top = 4.dp, start = 4.dp),
            text = latestItem.name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}