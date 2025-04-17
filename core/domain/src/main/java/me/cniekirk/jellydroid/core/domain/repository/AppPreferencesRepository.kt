package me.cniekirk.jellydroid.core.domain.repository

interface AppPreferencesRepository {

    suspend fun setTermsScreenShown(shown: Boolean)

    suspend fun setCurrentServer(serverId: String)

    suspend fun getCurrentServer(): String

    suspend fun setLoggedInUser(userId: String)

    suspend fun getLoggedInUser(): String
}