plugins {
    alias(libs.plugins.jellydroid.android.library)
    alias(libs.plugins.jellydroid.android.hilt)
}

android {
    namespace = "me.cniekirk.jellydroid.core.analytics"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.firebase.bom))
    implementation(libs.analytics)
    implementation(libs.crashlytics)

    testImplementation(libs.coroutines.test)
}