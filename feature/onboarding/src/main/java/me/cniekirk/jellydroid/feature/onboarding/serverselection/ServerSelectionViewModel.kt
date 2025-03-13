package me.cniekirk.jellydroid.feature.onboarding.serverselection

import androidx.lifecycle.ViewModel
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.jellydroid.core.data.repository.AuthenticationRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ServerSelectionViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel(), ContainerHost<ServerSelectionState, ServerSelectionEffect> {

    override val container = container<ServerSelectionState, ServerSelectionEffect>(ServerSelectionState())

    fun serverAddressChanged(address: String) = blockingIntent {
        reduce {
            state.copy(serverAddressText = address)
        }
    }

    fun connectToServer() = intent {
        reduce {
            state.copy(isLoading = true)
        }
        authenticationRepository.connectToServer(state.serverAddressText)
            .onSuccess { serverName ->
                Timber.d("Server name: $serverName")
                // Navigate to login
                postSideEffect(ServerSelectionEffect.NavigateToLogin(serverName))
            }
            .onFailure {
                // Couldn't connect
                Timber.e("Error occurred: $it")
                reduce {
                    state.copy(
                        isLoading = false,
                        serverErrorDialogDisplayed = true
                    )
                }
            }
    }

    fun dismissDialog() = intent {
        reduce {
            state.copy(serverErrorDialogDisplayed = false)
        }
    }
}
