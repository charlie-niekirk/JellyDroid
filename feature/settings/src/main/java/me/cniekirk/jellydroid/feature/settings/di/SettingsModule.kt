package me.cniekirk.jellydroid.feature.settings.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.cniekirk.jellydroid.feature.settings.usecase.GetSettingsOverviewUseCase
import me.cniekirk.jellydroid.feature.settings.usecase.GetSettingsOverviewUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SettingsModule {

    @Binds
    abstract fun bindGetSettingsOverviewUseCase(
        getSettingsOverviewUseCaseImpl: GetSettingsOverviewUseCaseImpl
    ): GetSettingsOverviewUseCase
}
