package me.cniekirk.jellydroid.feature.onboarding

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultExit
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

@Serializable
data object Onboarding

internal sealed interface OnboardingRoute {

    @Serializable
    data object Landing : OnboardingRoute

    @Serializable
    data object ServerSelection : OnboardingRoute

    @Serializable
    data class Login(val serverName: String) : OnboardingRoute
}

fun NavGraphBuilder.onboardingUserJourney(navHostController: NavHostController, navigateToHome: () -> Unit) {
    navigation<Onboarding>(
        startDestination = OnboardingRoute.Landing,
        exitTransition = { activityDefaultExit() }
    ) {

        composable<OnboardingRoute.Landing>(
            exitTransition = { exitAnimation() },
            popEnterTransition = { popEnterAnimation() }
        ) {
            val viewModel = hiltViewModel<LandingViewModel>()
            LandingRoute(
                viewModel = viewModel,
                navigateToServerSelection = { navHostController.navigate(OnboardingRoute.ServerSelection) },
                navigateToHome = { navigateToHome() }
            )
        }

        composable<OnboardingRoute.ServerSelection>(
            exitTransition = { exitAnimation() },
            popEnterTransition = { popEnterAnimation() },
            enterTransition = { enterAnimation() },
            popExitTransition = { popExitAnimation() }
        ) {
            val viewModel = hiltViewModel<ServerSelectionViewModel>()
            ServerSelectionRoute(viewModel) { serverName ->
                navHostController.navigate(OnboardingRoute.Login(serverName))
            }
        }

        composable<OnboardingRoute.Login>(
            exitTransition = { exitAnimation() },
            popEnterTransition = { popEnterAnimation() },
            enterTransition = { enterAnimation() },
            popExitTransition = { popExitAnimation() }
        ) {
            val viewModel = hiltViewModel<LoginViewModel>()
            LoginRoute(viewModel) {
                navigateToHome()
            }
        }
    }
}