package me.cniekirk.jellydroid.feature.onboarding.login

data class LoginState(
    val serverName: String,
    val isLoginErrorDialogDisplayed: Boolean = false,
    val isGenericErrorDialogDisplayed: Boolean = false
)

sealed interface LoginEffect {

    data object NavigateToHome : LoginEffect
}
