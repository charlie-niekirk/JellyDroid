package me.cniekirk.jellydroid.core.datastore.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.cniekirk.jellydroid.core.datastore.repository.AppPreferencesRepository
import me.cniekirk.jellydroid.core.datastore.repository.AppPreferencesRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBinding {

    @Binds
    abstract fun bindAppPreferencesRepository(appPreferencesRepositoryImpl: AppPreferencesRepositoryImpl): AppPreferencesRepository
}