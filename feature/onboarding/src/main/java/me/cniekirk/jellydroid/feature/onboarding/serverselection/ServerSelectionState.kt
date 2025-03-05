package me.cniekirk.jellydroid.feature.onboarding.serverselection

data class ServerSelectionState(
    val serverAddressText: String = "",
    val serverErrorDialogDisplayed: Boolean = false,
    val isLoading: Boolean = false
)

sealed interface ServerSelectionEffect {

    data class NavigateToLogin(val serverName: String) : ServerSelectionEffect
}
