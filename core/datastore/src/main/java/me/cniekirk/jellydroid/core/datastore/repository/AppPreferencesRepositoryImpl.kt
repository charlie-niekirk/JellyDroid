package me.cniekirk.jellydroid.core.datastore.repository

import androidx.datastore.core.DataStore
import me.cniekirk.jellydroid.core.datastore.AppPreferences
import javax.inject.Inject

class AppPreferencesRepositoryImpl @Inject constructor(
    private val datastore: DataStore<AppPreferences>
) : AppPreferencesRepository {

    override fun setTermsScreenShown(shown: Boolean) {

    }
}