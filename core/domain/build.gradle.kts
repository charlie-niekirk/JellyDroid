plugins {
    alias(libs.plugins.jellydroid.android.library)
    alias(libs.plugins.jellydroid.android.hilt)
    alias(libs.plugins.com.google.devtools.ksp)
}

android {
    namespace = "me.cniekirk.jellydroid.core.domain"
}

dependencies {
    api(projects.core.common)
    api(projects.core.model)
    api(projects.core.data)

    implementation(libs.kotlin.result)
    implementation(libs.kotlin.result.coroutines)
    implementation(libs.androidx.core.ktx)
    implementation(libs.org.jellyfin.sdk.jellyfin.core)

    testImplementation(libs.coroutines.test)
}