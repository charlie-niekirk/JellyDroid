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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import me.cniekirk.core.jellydroid.domain.model.AgeRating
import me.cniekirk.core.jellydroid.domain.model.MediaDetailsUiModel
import me.cniekirk.jellydroid.core.designsystem.theme.components.LoadableScreen
import me.cniekirk.jellydroid.core.designsystem.theme.preview.CoilPreview
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun MediaDetailsScreen(viewModel: MediaDetailsViewModel) {
    val state = viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MediaDetailsEffect.NavigateToPlayer -> {

            }
        }
    }

    MediaDetailsContent(state.value)
}

@Composable
private fun MediaDetailsContent(state: MediaDetailsState) {
    LoadableScreen(isLoading = state.isLoading) {
        if (state.mediaDetailsUiModel != null) {
            Success(state.mediaDetailsUiModel)
        } else {
            // TODO: Deal with unlikely case
        }
    }
}

@Composable
private fun Success(mediaDetailsUiModel: MediaDetailsUiModel) {
    val context = LocalPlatformContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 2f)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth(),
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
                0.8f to Color.Transparent,
                1.0f to MaterialTheme.colorScheme.background
            )
            val brush = Brush.verticalGradient(colorStops = colorStops)

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 2f),
                onDraw = { drawRect(brush = brush) }
            )
        }

        Text(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
            text = mediaDetailsUiModel.name,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            text = mediaDetailsUiModel.synopsis,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@PreviewLightDark
@Composable
private fun MediaDetailsContentPreview(@PreviewParameter(LoremIpsum::class) words: String) {
    val state = MediaDetailsState(
        isLoading = false,
        mediaDetailsUiModel = MediaDetailsUiModel(
            name = "Inception",
            synopsis = LoremIpsum(27).values.toList().first().toString(),
            primaryImageUrl = "",
            ageRating = AgeRating(
                ratingName = "12",
                ratingImageUrl = ""
            )
        )
    )
    CoilPreview {
        MediaDetailsContent(state)
    }
}