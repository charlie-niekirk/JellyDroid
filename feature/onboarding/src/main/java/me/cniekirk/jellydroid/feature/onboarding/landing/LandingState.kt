package me.cniekirk.jellydroid.feature.onboarding.landing

data class LandingState(
    val isLoading: Boolean = true,
    val analyticsChecked: Boolean = false,
    val crashlyticsChecked: Boolean = false
)

sealed interface LandingEffect {

    data object NavigateToServerSelection : LandingEffect
}