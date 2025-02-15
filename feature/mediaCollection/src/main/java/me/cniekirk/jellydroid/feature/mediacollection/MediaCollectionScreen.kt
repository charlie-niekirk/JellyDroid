package me.cniekirk.jellydroid.feature.mediacollection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            else -> {

            }
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
    TopBarPage(
        topBarTitle = state.collectionName,
        onBackClicked = { onBackClicked() }
    ) { paddingValues ->
        LoadableScreen(
            modifier = Modifier.padding(paddingValues),
            isLoading = state.isLoading
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(128.dp)
            ) {
                MediaItem(

                )
            }
        }
    }
}