package me.cniekirk.jellydroid.feature.settings.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import me.cniekirk.jellydroid.core.common.errors.LocalDataError
import me.cniekirk.jellydroid.core.data.repository.SettingsRepository
import me.cniekirk.jellydroid.feature.settings.model.SettingsOverviewUiModel
import javax.inject.Inject

internal class GetSettingsOverviewUseCaseImpl @Inject constructor(
    private val settingsRepository: SettingsRepository
) : GetSettingsOverviewUseCase {

    override suspend operator fun invoke(): Result<SettingsOverviewUiModel, LocalDataError> {
        return settingsRepository.getNumServers()
            .andThen { numServers ->
                settingsRepository.getNumUsers()
                    .map { numUsers ->
                        SettingsOverviewUiModel(
                            numServers = numServers,
                            numUsers = numUsers
                        )
                    }
            }
    }
}
