package me.cniekirk.jellydroid.core.datastore.repository

interface AppPreferencesRepository {

    suspend fun setTermsScreenShown(shown: Boolean)

    suspend fun setCurrentServer(serverId: String)

    suspend fun getCurrentServer(): String

    suspend fun setLoggedInUser(userId: String)

    suspend fun getLoggedInUser(): String
}