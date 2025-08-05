package me.cniekirk.jellydroid.core.domain.model.download

data class DownloadedMedia(
    val item: DownloadItem,
    val percentageDownloaded: Float,
    val isTerminal: Boolean,
    val isDownloading: Boolean
)
