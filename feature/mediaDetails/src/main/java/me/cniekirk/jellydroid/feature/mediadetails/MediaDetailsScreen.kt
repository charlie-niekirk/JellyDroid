package me.cniekirk.jellydroid.feature.mediadetails

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MovieCreation
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButtonShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
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
import kotlinx.coroutines.launch
import me.cniekirk.jellydroid.core.designsystem.theme.components.ErrorUi
import me.cniekirk.jellydroid.core.designsystem.theme.components.LoadableScreen
import me.cniekirk.jellydroid.core.designsystem.theme.components.TopBarPage
import me.cniekirk.jellydroid.core.designsystem.theme.preview.CoilPreview
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.AgeRating
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.CommunityRating
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.MediaAttributes
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.MediaDetails
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.Trailer
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.people.Person
import me.cniekirk.jellydroid.feature.mediadetails.components.Attributes
import me.cniekirk.jellydroid.feature.mediadetails.components.TrailerBottomSheetContent
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
        onPlayClicked = viewModel::onPlayClicked,
        onFavoriteClicked = viewModel::favoriteToggled,
        onDownloadClicked = viewModel::downloadClicked
    )
}

@Composable
private fun MediaDetailsContent(
    state: MediaDetailsState,
    onPlayClicked: () -> Unit,
    onFavoriteClicked: () -> Unit,
    onDownloadClicked: () -> Unit,
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
                    onPlayClicked = { onPlayClicked() },
                    onFavoriteClicked = { onFavoriteClicked() },
                    onDownloadClicked = { onDownloadClicked() }
                )
            } else if (state.error != null) {
                ErrorUi(error = state.error)
            }
        }
    }
}

const val THREE_BY_TWO_ASPECT_RATIO = 3f / 2f
const val MIDDLE_GRADIENT = 0.8f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Success(
    mediaDetails: MediaDetails,
    onPlayClicked: () -> Unit,
    onFavoriteClicked: () -> Unit,
    onDownloadClicked: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val trailerBottomSheetState = rememberModalBottomSheetState()
    var showTrailerBottomSheet by remember { mutableStateOf(false) }
    val handler = LocalUriHandler.current

    if (showTrailerBottomSheet) {
        ModalBottomSheet(
            sheetState = trailerBottomSheetState,
            onDismissRequest = {
                showTrailerBottomSheet = false
            }
        ) {
            TrailerBottomSheetContent(
                trailers = mediaDetails.trailers,
                onTrailerClicked = { url ->
                    handler.openUri(url)
                },
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
    ) {
        BackdropImage(imageUrl = mediaDetails.primaryImageUrl)
        ActionButtons(
            isFavorite = mediaDetails.isFavorite,
            onPlayClicked = onPlayClicked,
            onTrailerClicked = {
                coroutineScope.launch {
                    showTrailerBottomSheet = true
                }
            },
            onDownloadClicked = onDownloadClicked,
            onFavoriteClicked = onFavoriteClicked
        )
        Attributes(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            mediaAttributes = mediaDetails.mediaAttributes
        )
        Synopsis(text = mediaDetails.synopsis)
        PeopleList(people = mediaDetails.people)
    }
}

@Composable
private fun BackdropImage(imageUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(THREE_BY_TWO_ASPECT_RATIO)
    ) {
        val context = LocalPlatformContext.current

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(THREE_BY_TWO_ASPECT_RATIO),
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
            ),
            contentScale = ContentScale.FillHeight,
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
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ActionButtons(
    isFavorite: Boolean,
    onPlayClicked: () -> Unit,
    onTrailerClicked: () -> Unit,
    onDownloadClicked: () -> Unit,
    onFavoriteClicked: () -> Unit
) {
    Row(
        modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilledIconButton(
            modifier = Modifier
                .size(
                    IconButtonDefaults.mediumContainerSize(
                        widthOption = IconButtonDefaults.IconButtonWidthOption.Wide
                    )
                ),
            onClick = onPlayClicked,
            shape = IconButtonDefaults.mediumSquareShape
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = stringResource(R.string.play_button)
            )
        }

        FilledIconButton(
            modifier = Modifier
                .size(IconButtonDefaults.mediumContainerSize()),
            onClick = onTrailerClicked,
            shape = IconButtonDefaults.mediumSquareShape
        ) {
            Icon(
                imageVector = Icons.Default.MovieCreation,
                contentDescription = stringResource(R.string.trailer_button)
            )
        }

        FilledIconButton(
            modifier = Modifier
                .size(IconButtonDefaults.mediumContainerSize()),
            onClick = onDownloadClicked,
            shape = IconButtonDefaults.mediumSquareShape
        ) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = stringResource(R.string.download_button)
            )
        }

        FilledIconToggleButton(
            modifier = Modifier.size(IconButtonDefaults.mediumContainerSize()),
            checked = isFavorite,
            onCheckedChange = { onFavoriteClicked() },
            shapes = IconToggleButtonShapes(
                shape = IconButtonDefaults.mediumRoundShape,
                checkedShape = IconButtonDefaults.mediumSquareShape
            )
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = stringResource(R.string.trailer_button),
            )
        }
    }
}

@Composable
private fun Synopsis(text: String?) {
    text?.let {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = it,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun PeopleList(people: List<Person>) {
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
        items(people) { person ->
            Column {
                val context = LocalContext.current
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
            synopsis = LoremIpsum(PREVIEW_LOREM_IPSUM_LENGTH).values.toList().first(),
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
            people = listOf(),
            isFavorite = true,
            mediaName = "",
            trailers = listOf(
                Trailer("Trailer 1", "url1"),
                Trailer("Trailer 2", "url2")
            )
        )
    )
    CoilPreview {
        MediaDetailsContent(
            state = state,
            onPlayClicked = {},
            onBackClicked = {},
            onFavoriteClicked = {},
            onDownloadClicked = {}
        )
    }
}
