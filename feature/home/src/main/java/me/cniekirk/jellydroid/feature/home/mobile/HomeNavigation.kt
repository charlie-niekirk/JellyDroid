package me.cniekirk.jellydroid.feature.home.mobile

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.core.designsystem.theme.exitAnimation
import me.cniekirk.jellydroid.core.designsystem.theme.popEnterAnimation

fun NavGraphBuilder.home(
    onUserViewClicked: (String) -> Unit,
    onResumeItemClicked: (String) -> Unit,
    onMediaItemClicked: (String, String) -> Unit,
) {
    composable<Home>(
        exitTransition = { exitAnimation() },
        popEnterTransition = { popEnterAnimation() }
    ) {
        val viewModel = hiltViewModel<HomeViewModel>()
        HomeRoute(
            viewModel = viewModel,
            onUserViewClicked = { onUserViewClicked(it) },
            onResumeItemClicked = { onResumeItemClicked(it) },
            onMediaItemClicked = { id, name ->
                onMediaItemClicked(id, name)
            }
        )
    }
}

@Serializable
data object Home
