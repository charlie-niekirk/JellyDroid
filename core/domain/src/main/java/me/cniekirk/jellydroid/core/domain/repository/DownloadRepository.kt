package me.cniekirk.jellydroid.core.domain.repository

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.domain.model.error.DownloadError

interface DownloadRepository {

    suspend fun downloadMediaFile(url: String): Result<Long, DownloadError>
}