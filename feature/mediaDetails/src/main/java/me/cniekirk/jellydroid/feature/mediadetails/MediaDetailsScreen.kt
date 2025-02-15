package me.cniekirk.jellydroid.feature.mediadetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
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
    onBackClicked: () -> Unit
) {
    val state = viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MediaDetailsEffect.NavigateToPlayer -> {

            }
        }
    }

    MediaDetailsContent(state.value) {
        onBackClicked()
    }
}

@Composable
private fun MediaDetailsContent(
    state: MediaDetailsState,
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
                Success(state.mediaDetailsUiModel)
            } else {
                // TODO: Deal with unlikely case
            }
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
                    .fillMaxWidth()
                    .aspectRatio(3f / 2f),
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

@PreviewLightDark
@Composable
private fun MediaDetailsContentPreview(@PreviewParameter(LoremIpsum::class) words: String) {
    val title = "Inception"
    val state = MediaDetailsState(
        isLoading = false,
        mediaTitle = title,
        mediaDetailsUiModel = MediaDetailsUiModel(
            synopsis = LoremIpsum(27).values.toList().first().toString(),
            primaryImageUrl = "",
            mediaAttributes = MediaAttributes(
                ageRating = AgeRating(
                    ratingName = "12A",
                    ratingImageUrl = null
                ),
                communityRating = CommunityRating.StarRating(7.6f),
                runtime = "1h 32m"
            )
        )
    )
    CoilPreview {
        MediaDetailsContent(state) {}
    }
}