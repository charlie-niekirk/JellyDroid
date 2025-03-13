package me.cniekirk.jellydroid.feature.onboarding.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.cniekirk.jellydroid.core.designsystem.theme.JellyDroidTheme
import me.cniekirk.jellydroid.feature.onboarding.R
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    navigateToHome: () -> Unit
) {
    val state = viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            LoginEffect.NavigateToHome -> {
                navigateToHome()
            }
        }
    }

    LoginScreen(
        state = state.value,
        loginClicked = viewModel::loginToServer,
        loginErrorDialogDismissed = viewModel::dismissLoginError,
        genericErrorDialogDismissed = viewModel::dismissGenericError
    )
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginState,
    loginClicked: (String, String) -> Unit,
    loginErrorDialogDismissed: () -> Unit,
    genericErrorDialogDismissed: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier.padding(top = 32.dp, start = 32.dp),
            text = stringResource(id = R.string.login_title, state.serverName),
            style = MaterialTheme.typography.titleLarge,
        )

        var usernameInput by remember { mutableStateOf("") }
        var passwordInput by remember { mutableStateOf("") }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 32.dp),
            value = usernameInput,
            onValueChange = { usernameInput = it },
            label = { Text(stringResource(R.string.username)) },
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 8.dp),
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            modifier = Modifier.padding(start = 32.dp, top = 16.dp),
            onClick = { loginClicked(usernameInput, passwordInput) }
        ) {
            Text(stringResource(R.string.login_button))
        }

        if (state.isLoginErrorDialogDisplayed) {
            AlertDialog(
                onDismissRequest = { loginErrorDialogDismissed() },
                title = {
                    Text(stringResource(R.string.login_dialog_title))
                },
                text = {
                    Text(stringResource(R.string.login_dialog_body))
                },
                confirmButton = {
                    TextButton(
                        onClick = { loginErrorDialogDismissed() }
                    ) {
                        Text(stringResource(R.string.dialog_ok))
                    }
                }
            )
        }

        if (state.isGenericErrorDialogDisplayed) {
            AlertDialog(
                onDismissRequest = { genericErrorDialogDismissed() },
                title = {
                    Text(stringResource(R.string.generic_login_dialog_title))
                },
                text = {
                    Text(stringResource(R.string.generic_login_dialog_body))
                },
                confirmButton = {
                    TextButton(
                        onClick = { genericErrorDialogDismissed() }
                    ) {
                        Text(stringResource(R.string.dialog_ok))
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    JellyDroidTheme {
        Surface {
            LoginScreen(
                state = LoginState("abcde123"),
                loginClicked = { _, _ -> },
                loginErrorDialogDismissed = {},
                genericErrorDialogDismissed = {}
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenWithLoginDialogPreview() {
    JellyDroidTheme {
        Surface {
            LoginScreen(
                state = LoginState("abcde123", isLoginErrorDialogDisplayed = true),
                loginClicked = { _, _ -> },
                loginErrorDialogDismissed = {},
                genericErrorDialogDismissed = {}
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenWithGenericDialogPreview() {
    JellyDroidTheme {
        Surface {
            LoginScreen(
                state = LoginState("abcde123", isGenericErrorDialogDisplayed = true),
                loginClicked = { _, _ -> },
                loginErrorDialogDismissed = {},
                genericErrorDialogDismissed = {}
            )
        }
    }
}
