package me.cniekirk.jellydroid.feature.mediaplayer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.cniekirk.jellydroid.core.designsystem.theme.components.LoadableScreen
import me.cniekirk.jellydroid.feature.mediaplayer.components.MediaPlayer
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun MediaPlayerScreen(
    viewModel: MediaPlayerViewModel,
    onBackClicked: () -> Unit
) {
    val state = viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            else -> {}
        }
    }

    MediaPlayerContent(
        state = state.value,
        onBackClicked = { onBackClicked() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediaPlayerContent(
    state: MediaPlayerState,
    onBackClicked: () -> Unit
) {
    LoadableScreen(isLoading = state.isLoading) {
        val mediaUrl = state.mediaStreamUrl
        if (mediaUrl != null) {
            Column(modifier = Modifier.fillMaxSize()) {
                MediaPlayer(
                    modifier = Modifier.fillMaxSize(),
                    mediaUrl = mediaUrl
                )
            }
        }
    }
}

//TopAppBar(
//navigationIcon = {
//    Icon(
//        modifier = Modifier
//            .clickable { onBackClicked() }
//            .padding(
//                start = 16.dp,
//                end = 16.dp
//            ),
//        imageVector = Icons.AutoMirrored.Default.ArrowBack,
//        contentDescription = stringResource(R.string.back_button)
//    )
//},
//title = {
//    Text(
//        modifier = Modifier.padding(end = 16.dp),
//        text = state.title,
//        style = MaterialTheme.typography.titleLarge,
//        overflow = TextOverflow.Ellipsis
//    )
//}
//)