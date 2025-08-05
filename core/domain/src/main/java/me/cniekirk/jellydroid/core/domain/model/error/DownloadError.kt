package me.cniekirk.jellydroid.core.domain.model.error

sealed interface DownloadError {

    data object Unknown : DownloadError
}