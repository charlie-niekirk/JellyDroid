package me.cniekirk.jellydroid.feature.onboarding.login

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import me.cniekirk.jellydroid.core.domain.repository.AuthenticationRepository
import me.cniekirk.jellydroid.feature.onboarding.OnboardingNavigation
import org.junit.Before
import org.junit.Test
import org.orbitmvi.orbit.test.test

class LoginViewModelTest {

    private val authenticationRepository = mockk<AuthenticationRepository>()

    private lateinit var underTest: LoginViewModel

    @Before
    fun setup() {
        underTest = LoginViewModel(
            args = OnboardingNavigation.Login(SERVER_NAME),
            authenticationRepository = authenticationRepository
        )
    }

    @Test
    fun `on login success then verify NavigateToHome effect`() = runTest {
        // Given
        coEvery {
            authenticationRepository.authenticateUser(USERNAME, PASSWORD)
        } returns Ok(Unit)

        underTest.test(this) {
            expectInitialState()

            // When
            underTest.loginToServer(USERNAME, PASSWORD)

            // Then
            expectSideEffect(LoginEffect.NavigateToHome)
        }
    }

    @Test
    fun `on AuthenticationError login failure then verify isLoginErrorDialogDisplayed set true`() =
        runTest {
            // Given
            coEvery {
                authenticationRepository.authenticateUser(USERNAME, PASSWORD)
            } returns Err(NetworkError.AuthenticationError)

            underTest.test(this) {
                expectInitialState()

                // When
                underTest.loginToServer(USERNAME, PASSWORD)

                // Then
                expectState { copy(isLoginErrorDialogDisplayed = true) }
            }
        }

    @Test
    fun `on ClientConfigurationError login failure then verify isGenericErrorDialogDisplayed set true`() =
        runTest {
            // Given
            coEvery {
                authenticationRepository.authenticateUser(USERNAME, PASSWORD)
            } returns Err(NetworkError.ClientConfigurationError)

            underTest.test(this) {
                expectInitialState()

                // When
                underTest.loginToServer(USERNAME, PASSWORD)

                // Then
                expectState { copy(isGenericErrorDialogDisplayed = true) }
            }
        }

    @Test
    fun `on ConnectionError login failure then verify isGenericErrorDialogDisplayed set true`() =
        runTest {
            // Given
            coEvery {
                authenticationRepository.authenticateUser(USERNAME, PASSWORD)
            } returns Err(NetworkError.ConnectionError)

            underTest.test(this) {
                expectInitialState()

                // When
                underTest.loginToServer(USERNAME, PASSWORD)

                // Then
                expectState { copy(isGenericErrorDialogDisplayed = true) }
            }
        }

    @Test
    fun `on ServerError login failure then verify isGenericErrorDialogDisplayed set true`() =
        runTest {
            // Given
            coEvery { authenticationRepository.authenticateUser(USERNAME, PASSWORD) } returns Err(
                NetworkError.ServerError
            )

            underTest.test(this) {
                expectInitialState()

                // When
                underTest.loginToServer(USERNAME, PASSWORD)

                // Then
                expectState { copy(isGenericErrorDialogDisplayed = true) }
            }
        }

    @Test
    fun `on Unknown login failure then verify isGenericErrorDialogDisplayed set true`() = runTest {
        // Given
        coEvery { authenticationRepository.authenticateUser(USERNAME, PASSWORD) } returns Err(
            NetworkError.Unknown
        )

        underTest.test(this) {
            expectInitialState()

            // When
            underTest.loginToServer(USERNAME, PASSWORD)

            // Then
            expectState { copy(isGenericErrorDialogDisplayed = true) }
        }
    }

    @Test
    fun `on dismissLoginError then verify isLoginErrorDialogDisplayed set false`() = runTest {
        underTest.test(
            testScope = this,
            initialState = LoginState(serverName = SERVER_NAME, isLoginErrorDialogDisplayed = true)
        ) {
            expectInitialState()

            // When
            underTest.dismissLoginError()

            // Then
            expectState { copy(isLoginErrorDialogDisplayed = false) }
        }
    }

    @Test
    fun `on dismissGenericError then verify isGenericErrorDialogDisplayed set false`() = runTest {
        underTest.test(
            testScope = this,
            initialState = LoginState(
                serverName = SERVER_NAME,
                isGenericErrorDialogDisplayed = true
            )
        ) {
            expectInitialState()

            // When
            underTest.dismissGenericError()

            // Then
            expectState { copy(isGenericErrorDialogDisplayed = false) }
        }
    }

    companion object {
        const val SERVER_NAME = "jellyfin_server"
        const val USERNAME = "username"
        const val PASSWORD = "password"
    }
}
