plugins {
    alias(libs.plugins.jellydroid.android.feature)
    alias(libs.plugins.jellydroid.android.library.compose)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "me.cniekirk.jellydroid.feature.home"
}

dependencies {

    implementation(libs.androidx.tv.material)
    implementation(libs.immutable)
    implementation(libs.coil.compose)
    implementation(libs.immutable)
    implementation(libs.haze)
    implementation(libs.haze.materials)
}