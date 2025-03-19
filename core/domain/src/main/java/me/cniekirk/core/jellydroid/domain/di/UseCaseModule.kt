package me.cniekirk.core.jellydroid.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.cniekirk.core.jellydroid.domain.usecase.GetMediaDetailsUseCase
import me.cniekirk.core.jellydroid.domain.usecase.GetMediaDetailsUseCaseImpl
import me.cniekirk.core.jellydroid.domain.usecase.GetMediaCollectionUseCase
import me.cniekirk.core.jellydroid.domain.usecase.GetMediaCollectionUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseModule {

    @Binds
    abstract fun bindGetMediaDetailsUseCase(
        getMediaDetailsUseCaseImpl: GetMediaDetailsUseCaseImpl
    ): GetMediaDetailsUseCase

    @Binds
    abstract fun bindGetMoviesUseCase(
        getMoviesUseCaseImpl: GetMediaCollectionUseCaseImpl
    ): GetMediaCollectionUseCase
}
