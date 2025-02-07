package me.cniekirk.jellydroid.feature.home.mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.jellydroid.core.designsystem.theme.components.LoadingScreen
import me.cniekirk.jellydroid.core.designsystem.theme.preview.CoilPreview
import me.cniekirk.jellydroid.core.model.LatestItem
import me.cniekirk.jellydroid.core.model.ResumeItem
import me.cniekirk.jellydroid.core.model.UserView
import me.cniekirk.jellydroid.feature.home.HomeViewModel
import me.cniekirk.jellydroid.feature.home.R
import me.cniekirk.jellydroid.feature.home.mobile.components.LatestMovies
import me.cniekirk.jellydroid.feature.home.mobile.components.LatestShows
import me.cniekirk.jellydroid.feature.home.mobile.components.ResumeItems
import me.cniekirk.jellydroid.feature.home.mobile.components.UserViews
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
) {
    val state = viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is HomeEffect.ShowError -> {
                
            }
        }
    }

    HomeScreen(
        state = state.value,
        onQueryChange = viewModel::queryChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onQueryChange: (String) -> Unit
) {
    LoadingScreen(isLoading = state.isLoading) {
        Scaffold(
            modifier = Modifier.fillMaxSize().background(color = Color.White),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.home_title)) },
                    actions = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = stringResource(R.string.settings_content_description)
                            )
                        }
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
                        userViews = state.userViews
                    )
                }

                item {
                    ResumeItems(
                        modifier = Modifier.fillMaxWidth(),
                        resumeItems = state.resumeItems
                    )
                }

                item {
                    LatestMovies(
                        modifier = Modifier.fillMaxWidth(),
                        latestMovies = state.latestMovies
                    )
                }

                item {
                    LatestShows(
                        modifier = Modifier.fillMaxWidth(),
                        latestShows = state.latestShows
                    )
                }
            }
        }
    }
}

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
                    "",
                    1.7
                ),
                UserView(
                    "",
                    "",
                    "Shows",
                    "",
                    "",
                    "",
                    1.7
                )
            ),
            resumeItems = persistentListOf(
                ResumeItem(
                    "",
                    "Lord of the Rings: Return of the King",
                    "",
                    1.7,
                    44f
                ),
                ResumeItem(
                    "",
                    "Interstellar",
                    "",
                    1.7,
                    20f
                ),
                ResumeItem(
                    "",
                    "Inception",
                    "",
                    1.7,
                    80f
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
            onQueryChange = {}
        )
    }
}