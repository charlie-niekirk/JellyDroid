package me.cniekirk.jellydroid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import me.cniekirk.jellydroid.feature.onboarding.Onboarding
import me.cniekirk.jellydroid.feature.onboarding.onboardingUserJourney

@Composable
fun JellydroidNavHost(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Onboarding) {
        onboardingUserJourney(navHostController)
    }
}