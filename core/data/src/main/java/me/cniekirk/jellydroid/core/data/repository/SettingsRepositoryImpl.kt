package me.cniekirk.jellydroid.core.data.repository

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import me.cniekirk.jellydroid.core.database.dao.ServerDao
import me.cniekirk.jellydroid.core.domain.model.error.LocalDataError
import me.cniekirk.jellydroid.core.domain.repository.SettingsRepository
import javax.inject.Inject

internal class SettingsRepositoryImpl @Inject constructor(
    private val serverDao: ServerDao,
//    private val appPreferencesRepository: AppPreferencesRepository
) : SettingsRepository {

    override suspend fun getNumServers(): Result<Int, LocalDataError> {
        return runCatching {
            serverDao.getAllServersWithUsers().count()
        }.mapError {
            LocalDataError.DatabaseReadError
        }
    }

    override suspend fun getNumUsers(): Result<Int, LocalDataError> {
        return runCatching {
            serverDao.getAllServersWithUsers()
                .sumOf { server -> server.users.count() }
        }.mapError {
            LocalDataError.DatabaseReadError
        }
    }
}
