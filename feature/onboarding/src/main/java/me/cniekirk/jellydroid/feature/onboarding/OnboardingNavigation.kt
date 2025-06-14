package me.cniekirk.jellydroid.feature.onboarding

import androidx.compose.animation.togetherWith
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.core.designsystem.theme.enterAnimation
import me.cniekirk.jellydroid.core.designsystem.theme.exitAnimation
import me.cniekirk.jellydroid.core.designsystem.theme.popEnterAnimation
import me.cniekirk.jellydroid.core.designsystem.theme.popExitAnimation
import me.cniekirk.jellydroid.feature.onboarding.landing.LandingRoute
import me.cniekirk.jellydroid.feature.onboarding.landing.LandingViewModel
import me.cniekirk.jellydroid.feature.onboarding.login.LoginRoute
import me.cniekirk.jellydroid.feature.onboarding.login.LoginViewModel
import me.cniekirk.jellydroid.feature.onboarding.serverselection.ServerSelectionRoute
import me.cniekirk.jellydroid.feature.onboarding.serverselection.ServerSelectionViewModel

object OnboardingNavigation {

    interface OnboardingNavKey : NavKey

    @Serializable
    data object Landing : OnboardingNavKey

    @Serializable
    data object ServerSelection : OnboardingNavKey

    @Serializable
    data class Login(val serverName: String) : OnboardingNavKey
}

fun EntryProviderBuilder<NavKey>.onboardingModuleEntries(
    navBackStack: NavBackStack,
    navigateToHome: () -> Unit
) {
    entry<OnboardingNavigation.Landing>(
        metadata = NavDisplay.transitionSpec {
            enterAnimation() togetherWith exitAnimation()
        } + NavDisplay.popTransitionSpec {
            popEnterAnimation() togetherWith popExitAnimation()
        }
    ) {
        val viewModel = hiltViewModel<LandingViewModel>()
        LandingRoute(
            viewModel = viewModel,
            navigateToServerSelection = { navBackStack.add(OnboardingNavigation.ServerSelection) },
            navigateToHome = { navigateToHome() }
        )
    }

    entry<OnboardingNavigation.ServerSelection>(
        metadata = NavDisplay.transitionSpec {
            enterAnimation() togetherWith exitAnimation()
        } + NavDisplay.popTransitionSpec {
            popEnterAnimation() togetherWith popExitAnimation()
        }
    ) {
        val viewModel = hiltViewModel<ServerSelectionViewModel>()
        ServerSelectionRoute(
            viewModel = viewModel,
            navigateToLogin = { navBackStack.add(OnboardingNavigation.Login(it)) }
        )
    }

    entry<OnboardingNavigation.Login>(
        metadata = NavDisplay.transitionSpec {
            enterAnimation() togetherWith exitAnimation()
        } + NavDisplay.popTransitionSpec {
            popEnterAnimation() togetherWith popExitAnimation()
        }
    ) { key ->
        val viewModel = hiltViewModel<LoginViewModel, LoginViewModel.Factory>(
            creationCallback = { factory -> factory.create(key) }
        )
        LoginRoute(viewModel) {
            navigateToHome()
        }
    }
}
