package me.cniekirk.jellydroid.feature.mediaplayer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.cniekirk.jellydroid.feature.mediaplayer.usecase.GetPlaybackInfoUseCase
import me.cniekirk.jellydroid.feature.mediaplayer.usecase.GetPlaybackInfoUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaPlayerModule {

    @Binds
    abstract fun bindGetPlaybackInfoUseCase(getPlaybackInfoUseCaseImpl: GetPlaybackInfoUseCaseImpl): GetPlaybackInfoUseCase
}