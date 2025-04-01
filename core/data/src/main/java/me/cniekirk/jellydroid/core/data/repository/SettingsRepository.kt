package me.cniekirk.jellydroid.core.data.repository

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.common.errors.LocalDataError

interface SettingsRepository {

    suspend fun getNumServers(): Result<Int, LocalDataError>

    suspend fun getNumUsers(): Result<Int, LocalDataError>
}
