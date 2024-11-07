package me.cniekirk.jellydroid.feature.mediadetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun MediaDetailsScreen(viewModel: MediaDetailsViewModel) {
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
fun MediaDetailsContent(state: MediaDetailsState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        
    }
}