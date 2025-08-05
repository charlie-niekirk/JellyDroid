package me.cniekirk.jellydroid.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
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

    suspend fun getCurrentServer(): String = datastore.data.first().currentServer

    suspend fun setLoggedInUser(userId: String) {
        datastore.updateData { currentData ->
            currentData.toBuilder()
                .setCurrentUser(userId)
                .build()
        }
    }

    suspend fun getLoggedInUser(): String = datastore.data.first().currentUser

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

    suspend fun getAllDownloads(): List<AppPreferences.Download> = datastore.data.first().downloadedMediaList
}
