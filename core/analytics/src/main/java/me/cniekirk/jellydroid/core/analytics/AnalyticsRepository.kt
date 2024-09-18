package me.cniekirk.jellydroid.core.analytics

interface AnalyticsRepository {

    fun setAnalyticsEnabled(enabled: Boolean)

    fun setCrashlyticsEnabled(enabled: Boolean)

    fun logEvent(eventName: String, vararg parameters: Pair<String, String>)
}