package me.cniekirk.jellydroid.feature.home.mobile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.cniekirk.jellydroid.core.designsystem.theme.components.MediaItem
import me.cniekirk.jellydroid.core.domain.model.latest.LatestItem

@Composable
internal fun LatestMediaItems(
    sectionTitle: String,
    latestMedia: List<LatestItem>,
    onMediaItemClicked: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = sectionTitle,
            style = MaterialTheme.typography.titleMedium
        )

        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(latestMedia) { latestMovie ->
                MediaItem(
                    modifier = Modifier
                        .height(212.dp)
                        .width(IntrinsicSize.Min),
                    name = latestMovie.name,
                    imageUrl = latestMovie.imageUrl,
                    onMediaItemClicked = {
                        onMediaItemClicked(latestMovie.id, latestMovie.name)
                    }
                )
            }
        }
    }
}
