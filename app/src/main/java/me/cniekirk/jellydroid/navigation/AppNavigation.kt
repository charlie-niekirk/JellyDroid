package me.cniekirk.jellydroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import me.cniekirk.jellydroid.feature.onboarding.Onboarding
import me.cniekirk.jellydroid.feature.onboarding.onboardingUserJourney

@Composable
fun JellydroidNavHost(modifier: Modifier = Modifier, navHostController: NavHostController) {
    NavHost(modifier = modifier, navController = navHostController, startDestination = Onboarding) {
        onboardingUserJourney(navHostController)
    }
}