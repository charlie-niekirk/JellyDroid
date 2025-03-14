package me.cniekirk.jellydroid.feature.onboarding.landing

data class LandingState(
    val isLoading: Boolean = true,
)

sealed interface LandingEffect {

    data object NavigateToServerSelection : LandingEffect

    data object NavigateToHome : LandingEffect
}
