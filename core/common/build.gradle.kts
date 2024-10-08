plugins {
    alias(libs.plugins.jellydroid.android.library)
    alias(libs.plugins.jellydroid.android.hilt)
}

android {
    namespace = "me.cniekirk.jellydroid.core.common"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    testImplementation(libs.coroutines.test)
}