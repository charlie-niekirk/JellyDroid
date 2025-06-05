package me.cniekirk.jellydroid.feature.onboarding.serverselection

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.cniekirk.jellydroid.core.designsystem.theme.JellyDroidTheme
import me.cniekirk.jellydroid.core.designsystem.theme.components.LoadableScreen
import me.cniekirk.jellydroid.feature.onboarding.R
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ServerSelectionRoute(
    viewModel: ServerSelectionViewModel,
    navigateToLogin: (String) -> Unit
) {
    val state = viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ServerSelectionEffect.NavigateToLogin -> {
                navigateToLogin(sideEffect.serverName)
            }
        }
    }

    ServerSelectionScreen(
        state = state.value,
        onConnectClicked = viewModel::connectToServer,
        onDismissErrorDialog = viewModel::dismissErrorDialog
    )
}

@Composable
private fun ServerSelectionScreen(
    modifier: Modifier = Modifier,
    state: ServerSelectionState,
    onConnectClicked: (String) -> Unit,
    onDismissErrorDialog: () -> Unit
) {
    LoadableScreen(isLoading = state.isLoading) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.connect_title),
                style = MaterialTheme.typography.headlineMedium,
            )

            var serverAddress by remember { mutableStateOf("") }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = serverAddress,
                onValueChange = { text -> serverAddress = text },
                label = { Text(stringResource(R.string.server_address)) },
                placeholder = { Text(stringResource(R.string.server_address_placeholder)) }
            )

            Button(
                onClick = {
                    if (serverAddress.isNotBlank()) {
                        onConnectClicked(serverAddress)
                    }
                },
                enabled = serverAddress.isNotBlank(),
            ) {
                Text(stringResource(R.string.connect_button))
            }
        }
    }

    if (state.serverErrorDialogDisplayed) {
        AlertDialog(
            onDismissRequest = { onDismissErrorDialog() },
            title = {
                Text(stringResource(R.string.connect_dialog_title))
            },
            text = {
                Text(stringResource(R.string.connect_dialog_body))
            },
            confirmButton = {
                TextButton(
                    onClick = { onDismissErrorDialog() }
                ) {
                    Text(stringResource(R.string.dialog_ok))
                }
            }
        )
    }
}

@Preview
@Composable
private fun ServerSelectionScreenPreview() {
    JellyDroidTheme {
        Surface {
            ServerSelectionScreen(
                state = ServerSelectionState(isLoading = false),
                onConnectClicked = {},
                onDismissErrorDialog = {}
            )
        }
    }
}

@Preview
@Composable
private fun ServerSelectionScreenWithDialogPreview() {
    JellyDroidTheme {
        Surface {
            ServerSelectionScreen(
                state = ServerSelectionState(isLoading = false, serverErrorDialogDisplayed = true),
                onConnectClicked = {},
                onDismissErrorDialog = {}
            )
        }
    }
}
