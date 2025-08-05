package me.cniekirk.jellydroid.feature.mediacollection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.cniekirk.jellydroid.core.designsystem.theme.components.LoadableScreen
import me.cniekirk.jellydroid.core.designsystem.theme.components.TopBarPage
import me.cniekirk.jellydroid.core.designsystem.theme.preview.CoilPreview
import me.cniekirk.jellydroid.core.domain.model.Media
import me.cniekirk.jellydroid.feature.mediacollection.components.MediaCollectionItem
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun MediaCollectionRoute(
    viewModel: MediaCollectionViewModel,
    onBackClicked: () -> Unit
) {
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MediaCollectionEffect.ShowError -> {}
        }
    }

    MediaCollectionScreen(
        state = state,
        onBackClicked = { onBackClicked() }
    )
}

@Composable
private fun MediaCollectionScreen(
    state: MediaCollectionState,
    onBackClicked: () -> Unit
) {
    TopBarPage(
        topBarTitle = state.collectionName,
        onBackClicked = { onBackClicked() }
    ) { paddingValues ->
        LoadableScreen(
            modifier = Modifier.padding(paddingValues),
            isLoading = state.isLoading
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                columns = GridCells.Adaptive(minSize = 128.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.collectionItems) { item ->
                    MediaCollectionItem(
                        modifier = Modifier.fillMaxWidth(),
                        name = item.name,
                        imageUrl = item.thumbnailUrl,
                        onMediaItemClicked = {}
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MediaCollectionScreenPreview() {
    val state = MediaCollectionState(
        isLoading = false,
        collectionItems = listOf(
            Media(id = "1", name = "Interstellar", thumbnailUrl = ""),
            Media(id = "2", name = "Predator", thumbnailUrl = ""),
            Media(id = "3", name = "Lord of the Rings: The Return of the King", thumbnailUrl = ""),
            Media(id = "4", name = "The Big Lebowski", thumbnailUrl = ""),
            Media(id = "5", name = "The Wolf of Wall Street", thumbnailUrl = ""),
            Media(id = "6", name = "Inception", thumbnailUrl = "")
        ),
        collectionId = "collection123",
        collectionName = "Movies"
    )
    CoilPreview {
        MediaCollectionScreen(
            state = state,
            onBackClicked = {}
        )
    }
}
