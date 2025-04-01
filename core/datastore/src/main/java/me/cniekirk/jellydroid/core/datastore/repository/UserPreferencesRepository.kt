package me.cniekirk.jellydroid.core.datastore.repository

interface UserPreferencesRepository {

    suspend fun getDefaultMediaSort(): String
}
