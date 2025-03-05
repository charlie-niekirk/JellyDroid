package me.cniekirk.jellydroid.core.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.jellyfin.sdk.Jellyfin
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.HttpClientOptions
import org.jellyfin.sdk.createJellyfin
import org.jellyfin.sdk.model.ClientInfo
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Module
@InstallIn(SingletonComponent::class)
object JellyfinModule {

    @Provides
    @Singleton
    fun provideJellyfin(@ApplicationContext appContext: Context): Jellyfin {
        return createJellyfin {
            clientInfo = ClientInfo(name = "Jellydroid", version = "1.0.0")
            context = appContext
        }
    }

    @Provides
    @Singleton
    fun provideJellyfinApi(jellyfin: Jellyfin): ApiClient {
        return jellyfin.createApi(
            httpClientOptions = HttpClientOptions(requestTimeout = 10.seconds)
        )
    }
}
