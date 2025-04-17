package me.cniekirk.jellydroid.core.data.repository

import me.cniekirk.jellydroid.core.datastore.AppPreferencesDataSource
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

    override suspend fun getCurrentServer(): String =
        appPreferencesDataSource.getCurrentServer()

    override suspend fun setLoggedInUser(userId: String) {
        appPreferencesDataSource.setLoggedInUser(userId)
    }

    override suspend fun getLoggedInUser(): String =
        appPreferencesDataSource.getLoggedInUser()

}