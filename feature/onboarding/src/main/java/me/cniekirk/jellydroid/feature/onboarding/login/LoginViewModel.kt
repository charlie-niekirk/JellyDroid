package me.cniekirk.jellydroid.feature.onboarding.login

import androidx.lifecycle.ViewModel
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import me.cniekirk.jellydroid.core.domain.repository.AuthenticationRepository
import me.cniekirk.jellydroid.feature.onboarding.OnboardingNavigation
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel(assistedFactory = LoginViewModel.Factory::class)
class LoginViewModel @AssistedInject constructor(
    @Assisted private val args: OnboardingNavigation.Login,
    private val authenticationRepository: AuthenticationRepository
) : ViewModel(), ContainerHost<LoginState, LoginEffect> {

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

    @AssistedFactory
    interface Factory {
        fun create(args: OnboardingNavigation.Login): LoginViewModel
    }
}
