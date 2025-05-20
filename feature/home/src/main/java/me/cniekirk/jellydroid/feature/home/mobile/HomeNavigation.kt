package me.cniekirk.jellydroid.feature.home.mobile

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.core.designsystem.theme.exitAnimation
import me.cniekirk.jellydroid.core.designsystem.theme.popEnterAnimation
import me.cniekirk.jellydroid.core.domain.model.views.CollectionKind

fun NavGraphBuilder.home(
    onUserViewClicked: (String, String, CollectionKind) -> Unit,
    onResumeItemClicked: (String) -> Unit,
    onMediaItemClicked: (String, String) -> Unit,
    navigateToSettings: () -> Unit
) {
    composable<Home>(
        exitTransition = { exitAnimation() },
        popEnterTransition = { popEnterAnimation() }
    ) {
        val viewModel = hiltViewModel<HomeViewModel>()
        HomeRoute(
            viewModel = viewModel,
            onUserViewClicked = { id, name, kind -> onUserViewClicked(id, name, kind) },
            onResumeItemClicked = { onResumeItemClicked(it) },
            onMediaItemClicked = { id, name ->
                onMediaItemClicked(id, name)
            },
            navigateToSettings = { navigateToSettings() }
        )
    }
}

@Serializable
data object Home
