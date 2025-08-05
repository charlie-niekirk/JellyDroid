package me.cniekirk.jellydroid.core.designsystem.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.cniekirk.jellydroid.core.designsystem.R
import me.cniekirk.jellydroid.core.designsystem.theme.JellyDroidTheme
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError

@Composable
fun ErrorUi(
    error: NetworkError,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.error_title),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = getErrorMessage(error = error),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun getErrorMessage(error: NetworkError): String {
    return when (error) {
        NetworkError.AuthenticationError -> stringResource(id = R.string.error_authentication)
        NetworkError.ClientConfigurationError -> stringResource(id = R.string.error_client_configuration)
        NetworkError.ConnectionError -> stringResource(id = R.string.error_connection)
        NetworkError.ServerError -> stringResource(id = R.string.error_server)
        NetworkError.Unknown -> stringResource(id = R.string.error_unknown)
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorUiServerErrorPreview() {
    JellyDroidTheme {
        ErrorUi(error = NetworkError.ServerError)
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorUiAuthenticationErrorPreview() {
    JellyDroidTheme {
        ErrorUi(error = NetworkError.AuthenticationError)
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorUiClientConfigurationErrorPreview() {
    JellyDroidTheme {
        ErrorUi(error = NetworkError.ClientConfigurationError)
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorUiConnectionErrorPreview() {
    JellyDroidTheme {
        ErrorUi(error = NetworkError.ConnectionError)
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorUiUnknownErrorPreview() {
    JellyDroidTheme {
        ErrorUi(error = NetworkError.Unknown)
    }
}
