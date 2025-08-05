package me.cniekirk.jellydroid.feature.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.cniekirk.jellydroid.core.designsystem.theme.components.LoadableScreen
import me.cniekirk.jellydroid.core.designsystem.theme.components.MediaView
import me.cniekirk.jellydroid.core.domain.model.views.CollectionKind
import me.cniekirk.jellydroid.core.domain.model.views.UserView
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun LibraryRoute(
    viewModel: LibraryViewModel,
    navigateToCollection: (String, String, CollectionKind) -> Unit
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LibraryEffect.NavigateToLibrary -> {
                navigateToCollection(
                    sideEffect.collectionId,
                    sideEffect.collectionName,
                    sideEffect.collectionKind
                )
            }
        }
    }

    LibraryScreen(
        state = state,
        onUserViewClicked = viewModel::onUserViewClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LibraryScreen(
    state: LibraryState,
    onUserViewClicked: (UserView) -> Unit
) {
    LoadableScreen(isLoading = state.isLoading) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.library_title)) },
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Adaptive(minSize = 200.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.mediaLibraries) { item ->
                        MediaView(
                            name = item.name,
                            imageUrl = item.imageUrl,
                            aspectRatio = item.aspectRatio,
                            onUserViewClicked = { onUserViewClicked(item) }
                        )
                    }
                }
            }
        }
    }
}
