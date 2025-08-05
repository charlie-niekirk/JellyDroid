package me.cniekirk.jellydroid.feature.onboarding.serverselection

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import me.cniekirk.jellydroid.core.domain.model.servers.ServerConnection
import me.cniekirk.jellydroid.core.domain.repository.AppPreferencesRepository
import me.cniekirk.jellydroid.core.domain.repository.AuthenticationRepository
import org.junit.Before
import org.orbitmvi.orbit.test.test
import kotlin.test.Test

class ServerSelectionViewModelTest {

    private val authenticationRepository = mockk<AuthenticationRepository>()
    private val appPreferencesRepository = mockk<AppPreferencesRepository>()

    private lateinit var underTest: ServerSelectionViewModel

    @Before
    fun setup() {
        underTest = ServerSelectionViewModel(
            authenticationRepository = authenticationRepository,
            appPreferencesRepository = appPreferencesRepository
        )
    }

    @Test
    fun `when server connection successful then verify currentServer set & NavigateToLogin effect posted`() = runTest {
        coEvery { authenticationRepository.connectToServer(any()) } returns Ok(serverConnection)
        coJustRun { appPreferencesRepository.setCurrentServer(any()) }

        underTest.test(this) {
            underTest.connectToServer("http://example.com/server:8096")

            expectState {
                copy(isLoading = true)
            }

            expectSideEffect(ServerSelectionEffect.NavigateToLogin(SERVER_NAME))
        }

        coVerify(exactly = 1) { appPreferencesRepository.setCurrentServer(SERVER_ID) }
    }

    @Suppress("MaxLineLength")
    @Test
    fun `when server connection fails then verify isLoading set false & serverErrorDialogDisplayed set true`() = runTest {
        coEvery { authenticationRepository.connectToServer(any()) } returns Err(NetworkError.ConnectionError)

        underTest.test(this) {
            underTest.connectToServer("http://example.com/server:8096")

            expectState {
                copy(isLoading = true)
            }

            expectState {
                copy(
                    isLoading = false,
                    serverErrorDialogDisplayed = true
                )
            }
        }

        coVerify(exactly = 0) { appPreferencesRepository.setCurrentServer(any()) }
    }

    companion object {
        private const val SERVER_ID = "12345"
        private const val SERVER_NAME = "Some server"

        private val serverConnection = ServerConnection(
            serverId = SERVER_ID,
            serverName = SERVER_NAME
        )
    }
}
