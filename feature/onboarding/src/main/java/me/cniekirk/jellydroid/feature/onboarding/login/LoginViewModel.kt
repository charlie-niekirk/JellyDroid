package me.cniekirk.jellydroid.feature.onboarding.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.data.repository.JellyfinRepository
import me.cniekirk.jellydroid.feature.onboarding.OnboardingRoute
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val jellyfinRepository: JellyfinRepository
) : ViewModel(), ContainerHost<LoginState, LoginEffect> {

    private val args = savedStateHandle.toRoute<OnboardingRoute.Login>()

    override val container = container<LoginState, LoginEffect>(LoginState(serverName = args.serverName))

    fun usernameTextChanged(username: String) = blockingIntent {
        reduce {
            state.copy(usernameText = username)
        }
    }

    fun passwordTextChanged(password: String) = blockingIntent {
        reduce {
            state.copy(passwordText = password)
        }
    }

    fun loginToServer() = intent {
        jellyfinRepository.authenticateUser(state.usernameText, state.passwordText)
            .onSuccess {
                postSideEffect(LoginEffect.NavigateToHome)
            }
            .onFailure { error ->
                when (error) {
                    NetworkError.AuthenticationError -> {
                        reduce {
                            state.copy(isLoginErrorDialogDisplayed = true)
                        }
                    }
                    NetworkError.ClientConfigurationError,
                    NetworkError.ConnectionError,
                    NetworkError.ServerError,
                    NetworkError.Unknown -> {
                        reduce {
                            state.copy(isGenericErrorDialogDisplayed = true)
                        }
                    }
                }
            }
    }

    fun dismissLoginError() = intent {
        reduce {
            state.copy(isLoginErrorDialogDisplayed = false)
        }
    }

    fun dismissGenericError() = intent {
        reduce {
            state.copy(isGenericErrorDialogDisplayed = false)
        }
    }
}