plugins {
    alias(libs.plugins.jellydroid.android.library)
    alias(libs.plugins.jellydroid.android.hilt)
    alias(libs.plugins.com.google.devtools.ksp)
}

android {
    namespace = "me.cniekirk.jellydroid.core.data"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.core.domain)

    implementation(libs.kotlin.result)
    implementation(libs.kotlin.result.coroutines)

    implementation(libs.androidx.core.ktx)
    implementation(libs.org.jellyfin.sdk.jellyfin.core)

    testImplementation(libs.coroutines.test)
}