package me.cniekirk.jellydroid.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferencesDataSource @Inject constructor(
    private val datastore: DataStore<AppPreferences>
) {

    suspend fun setTermsScreenShown(shown: Boolean) {
        datastore.updateData { currentData ->
            currentData.toBuilder()
                .setTermsScreenShown(shown)
                .build()
        }
    }

    suspend fun setCurrentServer(serverId: String) {
        datastore.updateData { currentData ->
            currentData.toBuilder()
                .setCurrentServer(serverId)
                .build()
        }
    }

    fun getCurrentServer(): Flow<String> = datastore.data.map { it.currentServer }

    suspend fun setLoggedInUser(userId: String) {
        datastore.updateData { currentData ->
            currentData.toBuilder()
                .setCurrentUser(userId)
                .build()
        }
    }

    fun getLoggedInUser(): Flow<String> = datastore.data.map { it.currentUser }

    suspend fun addDownload(downloadId: String, mediaId: String, mediaName: String, mediaThumbnailUrl: String) {
        datastore.updateData { currentData ->
            val download = AppPreferences.Download
                .newBuilder()
                .setDownloadId(downloadId)
                .setMediaId(mediaId)
                .setMediaName(mediaName)
                .setMediaThumbnail(mediaThumbnailUrl)
                .build()

            currentData.toBuilder()
                .addDownloadedMedia(download)
                .build()
        }
    }

    fun getAllDownloads(): Flow<List<AppPreferences.Download>> = datastore.data.map { it.downloadedMediaList }
}
