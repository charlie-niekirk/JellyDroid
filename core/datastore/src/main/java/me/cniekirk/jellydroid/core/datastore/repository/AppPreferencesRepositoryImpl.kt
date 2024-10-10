package me.cniekirk.jellydroid.core.datastore.repository

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import me.cniekirk.jellydroid.core.datastore.AppPreferences
import javax.inject.Inject

class AppPreferencesRepositoryImpl @Inject constructor(
    private val datastore: DataStore<AppPreferences>
) : AppPreferencesRepository {

    override suspend fun setTermsScreenShown(shown: Boolean) {
        datastore.updateData { currentData ->
            currentData.toBuilder()
                .setTermsScreenShown(shown)
                .build()
        }
    }

    override suspend fun setCurrentServer(serverId: String) {
        datastore.updateData { currentData ->
            currentData.toBuilder()
                .setCurrentServer(serverId)
                .build()
        }
    }

    override suspend fun getCurrentServer(): String = datastore.data.first().currentServer

    override suspend fun setLoggedInUser(userId: String) {
        datastore.updateData { currentData ->
            currentData.toBuilder()
                .setCurrentUser(userId)
                .build()
        }
    }

    override suspend fun getLoggedInUser(): String = datastore.data.first().currentUser
}