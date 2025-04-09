package me.cniekirk.jellydroid.feature.onboarding.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.domain.repository.AuthenticationRepository
import me.cniekirk.jellydroid.feature.onboarding.OnboardingRoute
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authenticationRepository: AuthenticationRepository
) : ViewModel(), ContainerHost<LoginState, LoginEffect> {

    private val args = savedStateHandle.toRoute<OnboardingRoute.Login>()

    override val container = container<LoginState, LoginEffect>(LoginState(serverName = args.serverName))

    fun loginToServer(username: String, password: String) = intent {
        authenticationRepository.authenticateUser(username, password)
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
