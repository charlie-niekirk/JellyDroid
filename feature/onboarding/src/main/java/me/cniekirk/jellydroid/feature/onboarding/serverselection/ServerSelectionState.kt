package me.cniekirk.jellydroid.feature.onboarding.serverselection

data class ServerSelectionState(
    val isLoading: Boolean = false,
    val serverErrorDialogDisplayed: Boolean = false
)

sealed interface ServerSelectionEffect {

    data class NavigateToLogin(val serverName: String) : ServerSelectionEffect
}
