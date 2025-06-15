plugins {
    alias(libs.plugins.jellydroid.android.library)
}

android {
    namespace = "me.cniekirk.jellydroid.core.navigation"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.navigation3.runtime)
}