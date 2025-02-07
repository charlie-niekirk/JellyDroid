package me.cniekirk.jellydroid.feature.home.mobile

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.feature.home.HomeViewModel

@Serializable
data object Home

fun NavGraphBuilder.home(
    onUserViewClicked: (String) -> Unit,
    onResumeItemClicked: (String) -> Unit,
    onMediaItemClicked: (String) -> Unit,
) {
    composable<Home> {
        val viewModel = hiltViewModel<HomeViewModel>()
        HomeRoute(
            viewModel = viewModel,
            onUserViewClicked = { onUserViewClicked(it) },
            onResumeItemClicked = { onResumeItemClicked(it) },
            onMediaItemClicked = { onMediaItemClicked(it) },
        )
    }
}