package me.cniekirk.jellydroid.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.cniekirk.jellydroid.core.data.repository.AppPreferencesRepositoryImpl
import me.cniekirk.jellydroid.core.data.repository.AuthenticationRepositoryImpl
import me.cniekirk.jellydroid.core.data.repository.JellyfinRepositoryImpl
import me.cniekirk.jellydroid.core.data.repository.MediaRepositoryImpl
import me.cniekirk.jellydroid.core.data.repository.SettingsRepositoryImpl
import me.cniekirk.jellydroid.core.domain.repository.AppPreferencesRepository
import me.cniekirk.jellydroid.core.domain.repository.AuthenticationRepository
import me.cniekirk.jellydroid.core.domain.repository.JellyfinRepository
import me.cniekirk.jellydroid.core.domain.repository.MediaRepository
import me.cniekirk.jellydroid.core.domain.repository.SettingsRepository

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindJellyfinRepository(
        jellyfinRepositoryImpl: JellyfinRepositoryImpl
    ): JellyfinRepository

    @Binds
    abstract fun bindAuthenticationRepository(
        authenticationRepositoryImpl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    abstract fun bindMoviesRepository(
        moviesRepositoryImpl: MediaRepositoryImpl
    ): MediaRepository

    @Binds
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    abstract fun bindAppPreferencesRepository(
        appPreferencesRepositoryImpl: AppPreferencesRepositoryImpl
    ): AppPreferencesRepository
}
