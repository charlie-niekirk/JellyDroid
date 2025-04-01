package me.cniekirk.jellydroid.feature.home.mobile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.collections.immutable.ImmutableList
import me.cniekirk.jellydroid.core.designsystem.theme.preview.CoilPreview
import me.cniekirk.jellydroid.core.model.CollectionKind
import me.cniekirk.jellydroid.core.model.UserView
import me.cniekirk.jellydroid.feature.home.R

@Composable
internal fun UserViews(
    modifier: Modifier = Modifier,
    userViews: ImmutableList<UserView>,
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
                    userView = userView,
                    onUserViewClicked = { onUserViewClicked(userView.id, userView.name, userView.collectionKind) }
                )
            }
        }
    }
}

@Composable
internal fun MediaView(
    modifier: Modifier = Modifier,
    userView: UserView,
    onUserViewClicked: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.clickable { onUserViewClicked() },
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(userView.aspectRatio.toFloat())
                .clip(RoundedCornerShape(8.dp)),
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(userView.imageUrl)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop
            ),
            contentScale = ContentScale.Crop,
            contentDescription = userView.name
        )

        Text(
            modifier = Modifier.padding(top = 8.dp, start = 4.dp),
            text = userView.name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@PreviewLightDark
@Composable
private fun MediaViewPreview() {
    val userView = UserView(
        id = "0",
        parentId = "0",
        name = "The Big Lebowski",
        path = "",
        imageUrl = "",
        aspectRatio = (3f / 2f).toDouble(),
        collectionKind = CollectionKind.MOVIES
    )

    CoilPreview {
        MediaView(
            modifier = Modifier.padding(16.dp),
            userView = userView,
            onUserViewClicked = {}
        )
    }
}
