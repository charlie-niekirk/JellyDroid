package me.cniekirk.jellydroid.feature.onboarding

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.feature.onboarding.landing.LandingRoute
import me.cniekirk.jellydroid.feature.onboarding.landing.LandingViewModel
import me.cniekirk.jellydroid.feature.onboarding.login.LoginRoute
import me.cniekirk.jellydroid.feature.onboarding.login.LoginViewModel

@Serializable
data object Landing

@Serializable
data class Login(val serverName: String) : NavKey

fun EntryProviderBuilder<*>.landing(
    navigateToServerSelection: () -> Unit,
    navigateToHome: () -> Unit
) {
    entry<Landing> {
        val viewModel = hiltViewModel<LandingViewModel>()
        LandingRoute(
            viewModel = viewModel,
            navigateToServerSelection = { navigateToServerSelection() },
            navigateToHome = { navigateToHome() }
        )
    }
}

fun EntryProviderBuilder<*>.login(
    navigateToHome: () -> Unit
) {
    entry<Login> { key ->
        val viewModel = hiltViewModel<LoginViewModel>()
        LoginRoute(viewModel) {
            navigateToHome()
        }
    }
}
