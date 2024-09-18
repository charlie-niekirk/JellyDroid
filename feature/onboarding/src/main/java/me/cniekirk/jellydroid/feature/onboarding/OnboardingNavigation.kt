package me.cniekirk.jellydroid.feature.onboarding

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.feature.onboarding.landing.LandingRoute
import me.cniekirk.jellydroid.feature.onboarding.landing.LandingViewModel

@Serializable
data object Onboarding

internal sealed interface OnboardingRoute {

    @Serializable
    data object Landing : OnboardingRoute

    @Serializable
    data object ServerSelection : OnboardingRoute

    @Serializable
    data object Login : OnboardingRoute
}

fun NavGraphBuilder.onboardingUserJourney(navHostController: NavHostController) {
    navigation<Onboarding>(startDestination = OnboardingRoute.Landing) {

        composable<OnboardingRoute.Landing> {
            val viewModel = hiltViewModel<LandingViewModel>()
            LandingRoute(viewModel = viewModel) {
                navHostController.navigate(OnboardingRoute.ServerSelection)
            }
        }

        composable<OnboardingRoute.ServerSelection> {

        }

        composable<OnboardingRoute.Login> {

        }
    }
}