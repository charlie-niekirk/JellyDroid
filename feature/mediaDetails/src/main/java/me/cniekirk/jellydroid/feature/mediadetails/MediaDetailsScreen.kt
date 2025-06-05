package me.cniekirk.jellydroid.feature.mediadetails

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import me.cniekirk.jellydroid.core.designsystem.theme.components.LoadableScreen
import me.cniekirk.jellydroid.core.designsystem.theme.components.TopBarPage
import me.cniekirk.jellydroid.core.designsystem.theme.preview.CoilPreview
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.AgeRating
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.CommunityRating
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.MediaAttributes
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.MediaDetails
import me.cniekirk.jellydroid.feature.mediadetails.components.Attributes
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun MediaDetailsScreen(
    viewModel: MediaDetailsViewModel,
    onPlayClicked: (String) -> Unit,
    onBackClicked: () -> Unit
) {
    val state = viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MediaDetailsEffect.NavigateToPlayer -> {
                onPlayClicked(sideEffect.mediaId)
            }
        }
    }

    MediaDetailsContent(
        state = state.value,
        onBackClicked = { onBackClicked() },
        onPlayClicked = viewModel::onPlayClicked
    )
}

@Composable
private fun MediaDetailsContent(
    state: MediaDetailsState,
    onPlayClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    TopBarPage(
        topBarTitle = state.mediaTitle,
        onBackClicked = { onBackClicked() }
    ) { innerPadding ->
        LoadableScreen(
            modifier = Modifier.padding(innerPadding),
            isLoading = state.isLoading
        ) {
            if (state.mediaDetails != null) {
                Success(
                    mediaDetails = state.mediaDetails,
                    onPlayClicked = { onPlayClicked() }
                )
            } else {
                // TODO: Deal with unlikely case
            }
        }
    }
}

const val THREE_BY_TWO_ASPECT_RATIO = 3f / 2f
const val MIDDLE_GRADIENT = 0.8f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Success(mediaDetails: MediaDetails, onPlayClicked: () -> Unit) {
    val context = LocalPlatformContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(THREE_BY_TWO_ASPECT_RATIO)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(THREE_BY_TWO_ASPECT_RATIO),
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(mediaDetails.primaryImageUrl)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop
                ),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            val colorStops = arrayOf(
                0.0f to Color.Transparent,
                MIDDLE_GRADIENT to Color.Transparent,
                1.0f to MaterialTheme.colorScheme.background
            )
            val brush = Brush.verticalGradient(colorStops = colorStops)

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(THREE_BY_TWO_ASPECT_RATIO),
                onDraw = { drawRect(brush = brush) }
            )
        }

        IconButton(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp),
            onClick = { onPlayClicked() }
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = stringResource(R.string.play_button)
            )
        }

        Attributes(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            mediaAttributes = mediaDetails.mediaAttributes
        )

        mediaDetails.synopsis?.let { synopsis ->
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = synopsis,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            text = stringResource(R.string.media_details_people),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(mediaDetails.people) { person ->
                Column {
                    val drawable = remember(context, R.drawable.person_placeholder) {
                        ContextCompat.getDrawable(context, R.drawable.person_placeholder)
                    }

                    AsyncImage(
                        modifier = Modifier
                            .size(width = 120.dp, height = 180.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(person.imageUrl)
                            .crossfade(true)
                            .build(),
                        error = rememberDrawablePainter(drawable),
                        contentDescription = person.name,
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        modifier = Modifier
                            .width(120.dp)
                            .padding(top = 4.dp),
                        text = person.name,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Text(
                        modifier = Modifier.width(120.dp),
                        text = person.role,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

const val PREVIEW_STAR_RATING = 7.6f
const val PREVIEW_LOREM_IPSUM_LENGTH = 27

@PreviewLightDark
@Composable
private fun MediaDetailsContentPreview() {
    val title = "Inception"
    val state = MediaDetailsState(
        isLoading = false,
        mediaTitle = title,
        mediaDetails = MediaDetails(
            mediaId = "1",
            synopsis = LoremIpsum(PREVIEW_LOREM_IPSUM_LENGTH).values.toList().first().toString(),
            primaryImageUrl = "",
            mediaAttributes = MediaAttributes(
                ageRating = AgeRating(
                    ratingName = "12A",
                    ratingImageUrl = null
                ),
                communityRating = CommunityRating.StarRating(PREVIEW_STAR_RATING),
                runtime = "1h 32m",
            ),
            mediaPath = "",
            people = listOf()
        )
    )
    CoilPreview {
        MediaDetailsContent(
            state = state,
            onPlayClicked = {},
            onBackClicked = {}
        )
    }
}
