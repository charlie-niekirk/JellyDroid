package me.cniekirk.jellydroid.feature.settings

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultEnter
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultPopExit

fun NavGraphBuilder.settings(
    onBackPressed: () -> Unit
) {
    composable<Settings>(
        enterTransition = { activityDefaultEnter() },
        popExitTransition = { activityDefaultPopExit() }
    ) {
        val viewModel = hiltViewModel<SettingsViewModel>()
        SettingsRoute(
            viewModel = viewModel,
            navigateToPrivacyPolicy = {},
            navigateToAbout = {},
            onBackPressed = { onBackPressed() }
        )
    }
}

@Serializable
data object Settings
