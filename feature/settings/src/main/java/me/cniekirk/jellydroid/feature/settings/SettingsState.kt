package me.cniekirk.jellydroid.feature.settings

import me.cniekirk.jellydroid.feature.settings.model.SettingsError
import me.cniekirk.jellydroid.feature.settings.model.SettingsOverviewUiModel

internal data class SettingsState(
    val isLoading: Boolean = true,
    val settingsOverview: SettingsOverviewUiModel? = null
)

internal sealed interface SettingsEffect {

    data class Error(val errorType: SettingsError) : SettingsEffect
}
