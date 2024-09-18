plugins {
    alias(libs.plugins.jellydroid.android.library)
    alias(libs.plugins.jellydroid.android.hilt)
    alias(libs.plugins.com.google.devtools.ksp)
}

android {
    namespace = "me.cniekirk.jellydroid.core.network"
}

dependencies {
//    api(projects.core.common)

    implementation(libs.androidx.core.ktx)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.logging.interceptor)
    implementation(libs.mock.webserver)

    implementation(libs.org.jellyfin.sdk.jellyfin.core)

    implementation(libs.moshi)
    implementation(libs.moshi.adapters)
    ksp(libs.moshi.codegen)

    testImplementation(libs.coroutines.test)
}