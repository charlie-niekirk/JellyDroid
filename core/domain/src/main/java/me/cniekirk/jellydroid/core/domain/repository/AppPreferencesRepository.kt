package me.cniekirk.jellydroid.core.domain.repository

import kotlinx.coroutines.flow.Flow
import me.cniekirk.jellydroid.core.domain.model.download.DownloadItem

interface AppPreferencesRepository {

    suspend fun setTermsScreenShown(shown: Boolean)

    suspend fun setCurrentServer(serverId: String)

    fun getCurrentServer(): Flow<String>

    suspend fun setLoggedInUser(userId: String)

    fun getLoggedInUser(): Flow<String>

    suspend fun addDownload(
        downloadId: String,
        mediaId: String,
        mediaName: String,
        mediaThumbnailUrl: String
    )

    fun getAllDownloads(): Flow<List<DownloadItem>>
}