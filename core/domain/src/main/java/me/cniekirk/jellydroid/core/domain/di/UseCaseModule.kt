package me.cniekirk.jellydroid.core.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.cniekirk.jellydroid.core.domain.usecase.GetMediaCollectionUseCase
import me.cniekirk.jellydroid.core.domain.usecase.GetMediaCollectionUseCaseImpl
import me.cniekirk.jellydroid.core.domain.usecase.GetMediaDetailsUseCase
import me.cniekirk.jellydroid.core.domain.usecase.GetMediaDetailsUseCaseImpl

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
