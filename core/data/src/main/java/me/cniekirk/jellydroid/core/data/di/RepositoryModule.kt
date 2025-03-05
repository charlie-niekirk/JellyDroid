package me.cniekirk.jellydroid.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.cniekirk.jellydroid.core.data.repository.JellyfinRepository
import me.cniekirk.jellydroid.core.data.repository.JellyfinRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindJellyfinRepository(jellyfinRepositoryImpl: JellyfinRepositoryImpl): JellyfinRepository
}
