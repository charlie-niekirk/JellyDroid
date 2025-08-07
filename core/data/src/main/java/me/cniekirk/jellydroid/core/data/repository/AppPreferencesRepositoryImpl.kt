package me.cniekirk.jellydroid.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.cniekirk.jellydroid.core.data.mapping.toDownloadItem
import me.cniekirk.jellydroid.core.datastore.AppPreferencesDataSource
import me.cniekirk.jellydroid.core.domain.model.download.DownloadItem
import me.cniekirk.jellydroid.core.domain.repository.AppPreferencesRepository
import javax.inject.Inject

internal class AppPreferencesRepositoryImpl @Inject constructor(
    private val appPreferencesDataSource: AppPreferencesDataSource
) : AppPreferencesRepository {

    override suspend fun setTermsScreenShown(shown: Boolean) {
        appPreferencesDataSource.setTermsScreenShown(shown)
    }

    override suspend fun setCurrentServer(serverId: String) {
        appPreferencesDataSource.setCurrentServer(serverId)
    }

    override fun getCurrentServer(): Flow<String> =
        appPreferencesDataSource.getCurrentServer()

    override suspend fun setLoggedInUser(userId: String) {
        appPreferencesDataSource.setLoggedInUser(userId)
    }

    override fun getLoggedInUser(): Flow<String> =
        appPreferencesDataSource.getLoggedInUser()

    override suspend fun addDownload(
        downloadId: String,
        mediaId: String,
        mediaName: String,
        mediaThumbnailUrl: String
    ) {
        appPreferencesDataSource.addDownload(downloadId, mediaId, mediaName, mediaThumbnailUrl)
    }

    override fun getAllDownloads(): Flow<List<DownloadItem>> {
        return appPreferencesDataSource.getAllDownloads()
            .map { downloads -> downloads.map { it.toDownloadItem() } }
    }
}
