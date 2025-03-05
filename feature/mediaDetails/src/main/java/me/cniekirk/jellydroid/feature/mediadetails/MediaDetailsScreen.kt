package me.cniekirk.jellydroid.feature.mediadetails

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import me.cniekirk.core.jellydroid.domain.model.AgeRating
import me.cniekirk.core.jellydroid.domain.model.CommunityRating
import me.cniekirk.core.jellydroid.domain.model.MediaAttributes
import me.cniekirk.core.jellydroid.domain.model.MediaDetailsUiModel
import me.cniekirk.jellydroid.core.designsystem.theme.components.LoadableScreen
import me.cniekirk.jellydroid.core.designsystem.theme.components.TopBarPage
import me.cniekirk.jellydroid.core.designsystem.theme.preview.CoilPreview
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
            if (state.mediaDetailsUiModel != null) {
                Success(
                    mediaDetailsUiModel = state.mediaDetailsUiModel,
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

@Composable
private fun Success(mediaDetailsUiModel: MediaDetailsUiModel, onPlayClicked: () -> Unit) {
    val context = LocalPlatformContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
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
                        .data(mediaDetailsUiModel.primaryImageUrl)
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
            mediaAttributes = mediaDetailsUiModel.mediaAttributes
        )

        mediaDetailsUiModel.synopsis?.let { synopsis ->
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = synopsis,
                style = MaterialTheme.typography.bodyMedium
            )
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
        mediaDetailsUiModel = MediaDetailsUiModel(
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
            mediaPath = ""
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
