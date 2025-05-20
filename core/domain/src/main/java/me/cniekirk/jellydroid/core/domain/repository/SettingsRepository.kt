package me.cniekirk.jellydroid.core.domain.repository

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.domain.model.error.LocalDataError

interface SettingsRepository {

    suspend fun getNumServers(): Result<Int, LocalDataError>

    suspend fun getNumUsers(): Result<Int, LocalDataError>
}
