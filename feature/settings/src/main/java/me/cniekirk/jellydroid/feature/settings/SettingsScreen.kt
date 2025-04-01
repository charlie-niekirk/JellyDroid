package me.cniekirk.jellydroid.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.cniekirk.jellydroid.core.designsystem.theme.JellyDroidTheme
import me.cniekirk.jellydroid.core.designsystem.theme.components.LoadableScreen
import me.cniekirk.jellydroid.core.designsystem.theme.components.TopBarPage
import me.cniekirk.jellydroid.feature.settings.model.SettingsOverviewUiModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun SettingsRoute(
    viewModel: SettingsViewModel,
    navigateToPrivacyPolicy: () -> Unit,
    navigateToAbout: () -> Unit,
    onBackPressed: () -> Unit
) {
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SettingsEffect.NavigateToPrivacyPolicy -> { navigateToPrivacyPolicy() }
            is SettingsEffect.NavigateToAbout -> { navigateToAbout() }
            is SettingsEffect.Error -> {}
        }
    }

    SettingsScreen(
        state = state,
        onBackPressed = { onBackPressed() }
    )
}

@Composable
private fun SettingsScreen(state: SettingsState, onBackPressed: () -> Unit) {
    LoadableScreen(isLoading = state.isLoading) {
        TopBarPage(
            topBarTitle = stringResource(R.string.settings_title),
            onBackClicked = { onBackPressed() },
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp)
                )

                ListItem(
                    modifier = Modifier.padding(top = 8.dp),
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.settings_item_servers)
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null
                        )
                    },
                    trailingContent = {
                        state.settingsOverview?.let { settingsUiModel ->
                            Text(
                                text = settingsUiModel.numServers.toString()
                            )
                        }
                    },
                )

                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.settings_item_users)
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    trailingContent = {
                        state.settingsOverview?.let { settingsUiModel ->
                            Text(
                                text = settingsUiModel.numUsers.toString()
                            )
                        }
                    }
                )

                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.settings_item_appearance)
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null
                        )
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    JellyDroidTheme {
        Surface {
            SettingsScreen(
                state = SettingsState(
                    isLoading = false,
                    settingsOverview = SettingsOverviewUiModel(
                        numServers = 3,
                        numUsers = 5
                    )
                ),
                onBackPressed = {}
            )
        }
    }
}
