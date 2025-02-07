package me.cniekirk.core.jellydroid.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.cniekirk.core.jellydroid.domain.usecase.GetMediaDetailsUseCase
import me.cniekirk.core.jellydroid.domain.usecase.GetMediaDetailsUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindGetMediaDetailsUseCase(getMediaDetailsUseCaseImpl: GetMediaDetailsUseCaseImpl): GetMediaDetailsUseCase
}