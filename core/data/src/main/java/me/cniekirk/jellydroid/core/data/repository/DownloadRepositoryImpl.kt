package me.cniekirk.jellydroid.core.data.repository

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import me.cniekirk.jellydroid.core.data.R
import me.cniekirk.jellydroid.core.domain.model.error.DownloadError
import me.cniekirk.jellydroid.core.domain.repository.DownloadRepository
import javax.inject.Inject

internal class DownloadRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : DownloadRepository {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override suspend fun downloadMediaFile(url: String): Result<Long, DownloadError> {
        val fileName = url.substringAfterLast('/')

        val request = DownloadManager.Request(fileName.toUri())
            .setTitle(fileName)
            .setDescription(context.getString(R.string.downloads_description))
            // TODO: Inject preferences to determine these
//            .setAllowedOverMetered()
//            .setAllowedOverRoaming()
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                context.getString(R.string.jellydroid_folder_name)
            )

        return Ok(downloadManager.enqueue(request))
    }
}
