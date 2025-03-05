package me.cniekirk.jellydroid.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.cniekirk.jellydroid.core.datastore.AppPreferences
import me.cniekirk.jellydroid.core.datastore.AppPreferencesSerializer
import javax.inject.Singleton

private const val DATASTORE_FILE_NAME = "app_preferences.proto"

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    @Provides
    @Singleton
    fun provideAppPreferencesDatastore(@ApplicationContext context: Context): DataStore<AppPreferences> {
        return DataStoreFactory.create(
            serializer = AppPreferencesSerializer,
            produceFile = { context.dataStoreFile(DATASTORE_FILE_NAME) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }
}
