package me.cniekirk.jellydroid.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
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
    navigateBack: () -> Unit
) {
    val state = viewModel.collectAsState().value

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SettingsEffect.Error -> {}
        }
    }

    SettingsScreen(
        state = state,
        onAboutClicked = navigateToAbout,
        onPrivacyPolicyClicked = navigateToPrivacyPolicy,
        onBackClicked = navigateBack
    )
}

@Composable
private fun SettingsScreen(
    state: SettingsState,
    onAboutClicked: () -> Unit,
    onPrivacyPolicyClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    LoadableScreen(isLoading = state.isLoading) {
        TopBarPage(
            topBarTitle = stringResource(R.string.settings_title),
            onBackClicked = onBackClicked,
        ) { innerPadding ->
            SettingsContent(
                innerPadding = innerPadding,
                state = state,
                onAboutClicked = onAboutClicked,
                onPrivacyPolicyClicked = onPrivacyPolicyClicked
            )
        }
    }
}

@Composable
private fun SettingsContent(
    innerPadding: PaddingValues,
    state: SettingsState,
    onAboutClicked: () -> Unit,
    onPrivacyPolicyClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth() // Use fillMaxWidth for the main container
    ) {
        SettingsSectionHeader(text = stringResource(R.string.settings_jellyfin_label))

        SettingsItemWithCount(
            text = stringResource(R.string.settings_item_servers),
            count = state.settingsOverview?.numServers,
        )

        SettingsItemWithCount(
            text = stringResource(R.string.settings_item_users),
            count = state.settingsOverview?.numUsers,
        )

        SettingsItem(
            text = stringResource(R.string.settings_item_media),
            supportingText = stringResource(R.string.settings_item_media_description)
        )

        SettingsSectionHeader(text = stringResource(R.string.settings_app_label))

        SettingsItem(
            text = stringResource(R.string.settings_item_appearance),
            supportingText = stringResource(R.string.settings_item_appearance_description)
        )

        SettingsItem(
            text = stringResource(R.string.settings_item_network),
            supportingText = stringResource(R.string.settings_item_network_description)
        )

        SettingsItem(
            text = stringResource(R.string.settings_item_privacy_policy),
            onClick = onPrivacyPolicyClicked
        )

        SettingsItem(
            text = stringResource(R.string.settings_item_about),
            onClick = onAboutClicked
        )
    }
}

@Composable
private fun SettingsSectionHeader(text: String) {
    Text(
        modifier = Modifier.padding(top = 16.dp, start = 16.dp),
        text = text,
        style = MaterialTheme.typography.labelMedium
    )
    HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
}

@Composable
private fun SettingsItemWithCount(text: String, count: Int?) {
    ListItem(
        modifier = Modifier.padding(top = 8.dp),
        headlineContent = { Text(text = text) },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (count != null) {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = count.toString()
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun SettingsItem(text: String, supportingText: String? = null, onClick: (() -> Unit)? = null) {
    val modifier = if (onClick != null) {
        Modifier
            .clickable(onClick = onClick)
            .padding(top = 8.dp)
    } else {
        Modifier.padding(top = 8.dp)
    }
    ListItem(
        modifier = modifier,
        headlineContent = { Text(text = text) },
        supportingContent = supportingText?.let { { Text(text = it) } },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = null
            )
        }
    )
}

@PreviewLightDark
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
                onBackClicked = {},
                onAboutClicked = {},
                onPrivacyPolicyClicked = {}
            )
        }
    }
}
