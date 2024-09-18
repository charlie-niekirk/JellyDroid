package me.cniekirk.jellydroid.core.analytics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.cniekirk.jellydroid.core.analytics.AnalyticsRepository
import me.cniekirk.jellydroid.core.analytics.AnalyticsRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAnalyticsRepository(analyticsRepositoryImpl: AnalyticsRepositoryImpl): AnalyticsRepository
}