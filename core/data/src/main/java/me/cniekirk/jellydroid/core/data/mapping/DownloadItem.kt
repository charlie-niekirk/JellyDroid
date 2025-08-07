package me.cniekirk.jellydroid.core.data.mapping

import me.cniekirk.jellydroid.core.datastore.AppPreferences
import me.cniekirk.jellydroid.core.domain.model.download.DownloadItem

fun AppPreferences.Download.toDownloadItem(): DownloadItem {
    return DownloadItem(
        mediaId = this.mediaId,
        downloadId = this.downloadId,
        itemName = this.mediaName,
        mediaThumbnailUrl = this.mediaThumbnail
    )
}
