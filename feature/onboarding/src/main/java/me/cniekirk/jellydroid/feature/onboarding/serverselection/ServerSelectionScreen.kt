package me.cniekirk.jellydroid.feature.onboarding.serverselection

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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.cniekirk.jellydroid.core.designsystem.theme.JellyDroidTheme
import me.cniekirk.jellydroid.core.designsystem.theme.components.LoadingScreen
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
        onAddressChanged = viewModel::serverAddressChanged,
        connectClicked = viewModel::connectToServer,
        dismissErrorDialog = viewModel::dismissDialog
    )
}

@Composable
fun ServerSelectionScreen(
    modifier: Modifier = Modifier,
    state: ServerSelectionState,
    onAddressChanged: (String) -> Unit,
    connectClicked: () -> Unit,
    dismissErrorDialog: () -> Unit
) {
    if (state.isLoading) {
        LoadingScreen()
    } else {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.padding(top = 32.dp, start = 32.dp),
                text = stringResource(id = R.string.connect_title),
                style = MaterialTheme.typography.titleLarge,
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp, top = 32.dp),
                value = state.serverAddressText,
                onValueChange = { text -> onAddressChanged(text) },
                label = { Text(stringResource(R.string.server_address)) },
                placeholder = { Text(stringResource(R.string.server_address_placeholder)) }
            )

            Button(
                modifier = Modifier.padding(start = 32.dp, top = 16.dp),
                onClick = { connectClicked() }
            ) {
                Text(stringResource(R.string.connect_button))
            }
        }
    }

    if (state.serverErrorDialogDisplayed) {
        AlertDialog(
            onDismissRequest = { dismissErrorDialog() },
            title = {
                Text(stringResource(R.string.connect_dialog_title))
            },
            text = {
                Text(stringResource(R.string.connect_dialog_body))
            },
            confirmButton = {
                TextButton(
                    onClick = { dismissErrorDialog() }
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
                onAddressChanged = {},
                connectClicked = {},
                dismissErrorDialog = {}
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
                onAddressChanged = {},
                connectClicked = {},
                dismissErrorDialog = {}
            )
        }
    }
}