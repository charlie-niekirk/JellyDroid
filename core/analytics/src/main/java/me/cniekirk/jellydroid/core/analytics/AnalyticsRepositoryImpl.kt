package me.cniekirk.jellydroid.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class AnalyticsRepositoryImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val firebaseCrashlytics: FirebaseCrashlytics
) : AnalyticsRepository {

    override fun setAnalyticsEnabled(enabled: Boolean) {
        firebaseAnalytics.setAnalyticsCollectionEnabled(enabled)
    }

    override fun setCrashlyticsEnabled(enabled: Boolean) {
        firebaseCrashlytics.isCrashlyticsCollectionEnabled = enabled
    }

    override fun logEvent(eventName: String, vararg parameters: Pair<String, String>) {
        firebaseAnalytics.logEvent(eventName) {
            for (parameter in parameters) {
                param(parameter.first, parameter.second)
            }
        }
    }
}