package me.cniekirk.jellydroid.core.domain.model.download

data class DownloadItem(
    val mediaId: String,
    val downloadId: String,
    val itemName: String,
    val mediaThumbnailUrl: String
)