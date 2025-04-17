package me.cniekirk.jellydroid.feature.onboarding.serverselection

import androidx.lifecycle.ViewModel
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.jellydroid.core.domain.repository.AuthenticationRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ServerSelectionViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel(), ContainerHost<ServerSelectionState, ServerSelectionEffect> {

    override val container = container<ServerSelectionState, ServerSelectionEffect>(ServerSelectionState())

    fun connectToServer(serverAddress: String) = intent {
        reduce {
            state.copy(isLoading = true)
        }
        authenticationRepository.connectToServer(serverAddress)
            .onSuccess { serverName ->
                // Navigate to login
                postSideEffect(ServerSelectionEffect.NavigateToLogin(serverName))
            }
            .onFailure { error ->
                // Couldn't connect
                Timber.e("Error occurred while connecting to server: $error")
                reduce {
                    state.copy(
                        isLoading = false,
                        serverErrorDialogDisplayed = true
                    )
                }
            }
    }

    fun dismissErrorDialog() = intent {
        reduce {
            state.copy(serverErrorDialogDisplayed = false)
        }
    }
}
