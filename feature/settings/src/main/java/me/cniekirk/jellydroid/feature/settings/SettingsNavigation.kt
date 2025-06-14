package me.cniekirk.jellydroid.feature.settings

import androidx.compose.animation.togetherWith
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultEnter
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultExit
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultPopEnter
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultPopExit

fun EntryProviderBuilder<*>.settings(
    onBackPressed: () -> Unit
) {
    entry<Settings>(
        metadata = NavDisplay.transitionSpec {
            activityDefaultEnter() togetherWith activityDefaultExit()
        } + NavDisplay.popTransitionSpec {
            activityDefaultPopEnter() togetherWith activityDefaultPopExit()
        },
    ) {
        val viewModel = hiltViewModel<SettingsViewModel>()
        SettingsRoute(
            viewModel = viewModel,
            navigateToPrivacyPolicy = {},
            navigateToAbout = {},
            navigateBack = onBackPressed
        )
    }
}

@Serializable
data object Settings : NavKey
