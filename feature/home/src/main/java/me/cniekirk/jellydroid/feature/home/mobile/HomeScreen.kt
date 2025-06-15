package me.cniekirk.jellydroid.feature.home.mobile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.jellydroid.core.designsystem.theme.components.LoadableScreen
import me.cniekirk.jellydroid.core.designsystem.theme.preview.CoilPreview
import me.cniekirk.jellydroid.core.domain.model.ResumeItem
import me.cniekirk.jellydroid.core.domain.model.latest.LatestItem
import me.cniekirk.jellydroid.core.domain.model.views.CollectionKind
import me.cniekirk.jellydroid.core.domain.model.views.UserView
import me.cniekirk.jellydroid.feature.home.R
import me.cniekirk.jellydroid.feature.home.mobile.components.LatestMediaItems
import me.cniekirk.jellydroid.feature.home.mobile.components.ResumeItems
import me.cniekirk.jellydroid.feature.home.mobile.components.UserViews
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel,
    onUserViewClicked: (String, String, CollectionKind) -> Unit,
    onResumeItemClicked: (String) -> Unit,
    onMediaItemClicked: (String, String) -> Unit,
    navigateToSettings: () -> Unit
) {
    val state = viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is HomeEffect.ShowError -> {}
        }
    }

    HomeScreen(
        state = state.value,
        onUserViewClicked = { id, name, kind -> onUserViewClicked(id, name, kind) },
        onResumeItemClicked = { onResumeItemClicked(it) },
        onMediaItemClicked = { id, name ->
            onMediaItemClicked(id, name)
        },
        onSettingsClicked = { navigateToSettings() }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun HomeScreen(
    state: HomeState,
    onUserViewClicked: (String, String, CollectionKind) -> Unit,
    onResumeItemClicked: (String) -> Unit,
    onMediaItemClicked: (String, String) -> Unit,
    onSettingsClicked: () -> Unit
) {
    LoadableScreen(isLoading = state.isLoading) {
        Scaffold(
            modifier = Modifier.fillMaxSize().background(color = Color.White),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.home_title)) },
                    actions = {
                        Image(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(42.dp)
                                .clip(MaterialShapes.Cookie9Sided.toShape())
                                .clickable { onSettingsClicked() },
                            painter = rememberAsyncImagePainter(state.userProfileImage),
                            contentDescription = stringResource(R.string.profile_image)
                        )
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Transparent),
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    UserViews(
                        modifier = Modifier.fillMaxWidth(),
                        userViews = state.userViews,
                        onUserViewClicked = { id, name, kind -> onUserViewClicked(id, name, kind) }
                    )
                }

                item {
                    ResumeItems(
                        modifier = Modifier.fillMaxWidth(),
                        resumeItems = state.resumeItems,
                        onResumeItemClicked = { onResumeItemClicked(it) }
                    )
                }

                item {
                    LatestMediaItems(
                        modifier = Modifier.fillMaxWidth(),
                        latestMedia = state.latestMovies,
                        sectionTitle = stringResource(R.string.latest_movies_title),
                        onMediaItemClicked = { id, name ->
                            onMediaItemClicked(id, name)
                        }
                    )
                }

                item {
                    LatestMediaItems(
                        modifier = Modifier.fillMaxWidth(),
                        latestMedia = state.latestShows,
                        sectionTitle = stringResource(R.string.latest_shows_title),
                        onMediaItemClicked = { id, name ->
                            onMediaItemClicked(id, name)
                        }
                    )
                }
            }
        }
    }
}

const val PREVIEW_ASPECT_RATIO = 1.7
const val PREVIEW_PLAYED_PERCENTAGE = 44f

// TODO: Refactor
@Suppress("LongMethod")
@PreviewLightDark
@Composable
private fun HomeScreenPreview() {
    CoilPreview {
        val state = HomeState(
            isLoading = false,
            userViews = persistentListOf(
                UserView(
                    "",
                    "",
                    "Movies",
                    "",
                    "",
                    PREVIEW_ASPECT_RATIO,
                    CollectionKind.MOVIES
                ),
                UserView(
                    "",
                    "",
                    "Shows",
                    "",
                    "",
                    PREVIEW_ASPECT_RATIO,
                    CollectionKind.SERIES
                )
            ),
            resumeItems = persistentListOf(
                ResumeItem(
                    "",
                    "Lord of the Rings: Return of the King",
                    "",
                    PREVIEW_ASPECT_RATIO,
                    PREVIEW_PLAYED_PERCENTAGE
                ),
                ResumeItem(
                    "",
                    "Interstellar",
                    "",
                    PREVIEW_ASPECT_RATIO,
                    PREVIEW_PLAYED_PERCENTAGE
                ),
                ResumeItem(
                    "",
                    "Inception",
                    "",
                    PREVIEW_ASPECT_RATIO,
                    PREVIEW_PLAYED_PERCENTAGE
                )
            ),
            latestMovies = persistentListOf(
                LatestItem(
                    id = "",
                    name = "Inception",
                    imageUrl = ""
                ),
                LatestItem(
                    id = "",
                    name = "Interstellar",
                    imageUrl = ""
                ),
                LatestItem(
                    id = "",
                    name = "The Matrix",
                    imageUrl = ""
                )
            ),
            latestShows = persistentListOf(
                LatestItem(
                    id = "",
                    name = "Game of Thrones",
                    imageUrl = ""
                ),
                LatestItem(
                    id = "",
                    name = "Obi Wan Kenobi",
                    imageUrl = ""
                ),
                LatestItem(
                    id = "",
                    name = "Bob's Burgers",
                    imageUrl = ""
                )
            )
        )

        HomeScreen(
            state = state,
            onUserViewClicked = { _, _, _ -> },
            onResumeItemClicked = {},
            onMediaItemClicked = { _, _ -> },
            onSettingsClicked = {}
        )
    }
}
