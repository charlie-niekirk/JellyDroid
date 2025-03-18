package me.cniekirk.jellydroid.feature.mediacollection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.cniekirk.jellydroid.core.designsystem.theme.components.LoadableScreen
import me.cniekirk.jellydroid.core.designsystem.theme.components.MediaItem
import me.cniekirk.jellydroid.core.designsystem.theme.components.TopBarPage
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun MediaCollectionRoute(viewModel: MediaCollectionViewModel) {
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MediaCollectionEffect.ShowError -> {}
        }
    }

    MediaCollectionScreen(
        state = state,
        onBackClicked = {}
    )
}

@Composable
private fun MediaCollectionScreen(
    state: MediaCollectionState,
    onBackClicked: () -> Unit
) {
    val title = when (state.collectionType) {
        CollectionType.MOVIES -> stringResource(R.string.title_movies)
        CollectionType.TV_SHOWS -> stringResource(R.string.title_shows)
    }

    TopBarPage(
        topBarTitle = title,
        onBackClicked = { onBackClicked() }
    ) { paddingValues ->
        LoadableScreen(
            modifier = Modifier.padding(paddingValues),
            isLoading = state.isLoading
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(128.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.collectionItems) { item ->
                    MediaItem(
                        name = item.name,
                        imageUrl = item.thumbnailUrl,
                        onMediaItemClicked = {}
                    )
                }
            }
        }
    }
}
