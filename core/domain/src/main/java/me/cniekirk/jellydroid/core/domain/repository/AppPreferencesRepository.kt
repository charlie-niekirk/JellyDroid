package me.cniekirk.jellydroid.core.domain.repository

import me.cniekirk.jellydroid.core.domain.model.download.DownloadItem

interface AppPreferencesRepository {

    suspend fun setTermsScreenShown(shown: Boolean)

    suspend fun setCurrentServer(serverId: String)

    suspend fun getCurrentServer(): String

    suspend fun setLoggedInUser(userId: String)

    suspend fun getLoggedInUser(): String

    suspend fun addDownload(
        downloadId: String,
        mediaId: String,
        mediaName: String,
        mediaThumbnailUrl: String
    )

    suspend fun getAllDownloads(): List<DownloadItem>
}