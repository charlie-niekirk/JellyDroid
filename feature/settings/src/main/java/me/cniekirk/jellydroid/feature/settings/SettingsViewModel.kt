package me.cniekirk.jellydroid.feature.settings

import androidx.lifecycle.ViewModel
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.jellydroid.feature.settings.model.SettingsError
import me.cniekirk.jellydroid.feature.settings.usecase.GetSettingsOverviewUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val getSettingsOverviewUseCase: GetSettingsOverviewUseCase
) : ViewModel(), ContainerHost<SettingsState, SettingsEffect> {

    override val container = container<SettingsState, SettingsEffect>(SettingsState()) {
        loadSettings()
    }

    private fun loadSettings() = intent {
        getSettingsOverviewUseCase()
            .onSuccess { settingsOverview ->
                reduce {
                    state.copy(
                        isLoading = false,
                        settingsOverview = settingsOverview
                    )
                }
            }
            .onFailure {
                reduce {
                    state.copy(isLoading = false)
                }
                postSideEffect(SettingsEffect.Error(SettingsError.FETCH_SETTINGS_FAILED))
            }
    }
}
