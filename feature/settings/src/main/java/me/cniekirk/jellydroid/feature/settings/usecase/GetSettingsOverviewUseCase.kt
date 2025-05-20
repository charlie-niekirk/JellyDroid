package me.cniekirk.jellydroid.feature.settings.usecase

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.domain.model.error.LocalDataError
import me.cniekirk.jellydroid.feature.settings.model.SettingsOverviewUiModel

internal interface GetSettingsOverviewUseCase {

    suspend operator fun invoke(): Result<SettingsOverviewUiModel, LocalDataError>
}
