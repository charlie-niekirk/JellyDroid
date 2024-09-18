package me.cniekirk.jellydroid.feature.onboarding.landing

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import me.cniekirk.jellydroid.core.designsystem.theme.components.CheckboxItem
import me.cniekirk.jellydroid.core.designsystem.theme.JellyDroidTheme
import me.cniekirk.jellydroid.core.designsystem.theme.components.LoadingScreen
import me.cniekirk.jellydroid.feature.onboarding.R
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LandingRoute(
    viewModel: LandingViewModel,
    navigateToServerSelection: () -> Unit
) {
    val state = viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            LandingEffect.NavigateToServerSelection -> {
                navigateToServerSelection()
            }
        }
    }

    AnimatedContent(state.value.isLoading, label = "Loading animation") { isLoading ->
        if (isLoading) {
            LoadingScreen()
        } else {
            LandingScreen(
                state = state.value,
                onAnalyticsCheckedChanged = viewModel::analyticsCheckedChanged,
                onCrashlyticsCheckedChanged = viewModel::crashlyticsCheckedChanged,
                onContinueClicked = viewModel::onContinueButtonPressed
            )
        }
    }
}

@Composable
fun LandingScreen(
    state: LandingState,
    onAnalyticsCheckedChanged: (Boolean) -> Unit,
    onCrashlyticsCheckedChanged: (Boolean) -> Unit,
    onContinueClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.padding(top = 32.dp, start = 32.dp),
            text = stringResource(id = R.string.welcome_message),
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            modifier = Modifier.padding(top = 16.dp, start = 32.dp),
            text = stringResource(id = R.string.data_collection_title),
            style = MaterialTheme.typography.titleSmall,
        )

        Text(
            text = stringResource(R.string.data_collection_explanation),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 16.dp),
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.weight(1f))

        HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))

        CheckboxItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = stringResource(R.string.analytics_explanation),
            checked = state.analyticsChecked,
            onCheckedChanged = { onAnalyticsCheckedChanged(it) }
        )

        CheckboxItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            text = stringResource(R.string.crashlytics_explanation),
            checked = state.crashlyticsChecked,
            onCheckedChanged = { onCrashlyticsCheckedChanged(it) }
        )

        Button(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .align(Alignment.CenterHorizontally),
            onClick = { onContinueClicked() }
        ) {
            Text(
                text = stringResource(R.string.continue_button)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun LandingScreenPreview() {
    JellyDroidTheme {
        Surface {
            LandingScreen(
                state = LandingState(
                    analyticsChecked = true,
                    crashlyticsChecked = false
                ),
                onAnalyticsCheckedChanged = {},
                onCrashlyticsCheckedChanged = {},
                onContinueClicked = {}
            )
        }
    }
}