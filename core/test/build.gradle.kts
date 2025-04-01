plugins {
    alias(libs.plugins.jellydroid.android.library)
}

android {
    namespace = "me.cniekirk.jellydroid.core.test"

    packaging {
        resources {
            excludes += "META-INF/*"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coroutines.test)
    implementation(libs.androidx.junit)
    implementation(libs.androidx.junit.ext)
    implementation(libs.mockk)
}