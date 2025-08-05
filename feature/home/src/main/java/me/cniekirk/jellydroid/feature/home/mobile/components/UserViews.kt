package me.cniekirk.jellydroid.feature.home.mobile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.cniekirk.jellydroid.core.designsystem.theme.JellyDroidTheme
import me.cniekirk.jellydroid.core.designsystem.theme.components.MediaView
import me.cniekirk.jellydroid.core.domain.model.views.CollectionKind
import me.cniekirk.jellydroid.core.domain.model.views.UserView
import me.cniekirk.jellydroid.feature.home.R

@Composable
internal fun UserViews(
    modifier: Modifier = Modifier,
    userViews: List<UserView>,
    onUserViewClicked: (String, String, CollectionKind) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.media_title),
            style = MaterialTheme.typography.titleMedium
        )

        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(userViews) { userView ->
                MediaView(
                    modifier = Modifier.width(212.dp),
                    name = userView.name,
                    imageUrl = userView.imageUrl,
                    aspectRatio = userView.aspectRatio,
                    onUserViewClicked = { onUserViewClicked(userView.id, userView.name, userView.collectionKind) }
                )
            }
        }
    }
}

@Preview
@Composable
internal fun UserViewsPreview() {
    val userViews = listOf(
        UserView(
            id = "1",
            parentId = "parent1",
            name = "Movie 1",
            path = "/path/to/movie1",
            imageUrl = "https://example.com/image1.jpg",
            aspectRatio = 1.77,
            collectionKind = CollectionKind.MOVIES
        ),
        UserView(
            id = "2",
            parentId = "parent2",
            name = "Series 1",
            path = "/path/to/series1",
            imageUrl = "https://example.com/image2.jpg",
            aspectRatio = 1.77,
            collectionKind = CollectionKind.SERIES
        )
    )

    JellyDroidTheme {
        Surface {
            UserViews(
                modifier = Modifier.padding(16.dp),
                userViews = userViews,
                onUserViewClicked = { _, _, _ -> }
            )
        }
    }
}
