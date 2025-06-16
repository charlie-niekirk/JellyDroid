plugins {
    alias(libs.plugins.jellydroid.android.application)
    alias(libs.plugins.jellydroid.android.application.compose)
    alias(libs.plugins.jellydroid.android.hilt)
    alias(libs.plugins.com.google.gms.google.services)
    alias(libs.plugins.com.google.firebase.crashlytics)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
}

android {
    namespace = "me.cniekirk.jellydroid"
    compileSdk = 36

    defaultConfig {
        applicationId = "me.cniekirk.jellydroid"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/*"
        }
    }
    ksp {
        arg("room.generateKotlin", "true")
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
    implementation(projects.feature.onboarding)
    implementation(projects.feature.home)
    implementation(projects.feature.mediaCollection)
    implementation(projects.feature.mediaDetails)
    implementation(projects.feature.mediaPlayer)
    implementation(projects.feature.settings)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.org.jetbrains.kotlinx.serialization)
    implementation(libs.org.jetbrains.kotlinx.serialization.json)

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.material3.navigation3)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    implementation(libs.ktor.android)
    implementation(libs.coil.network.ktor2)

    implementation(libs.material.icons)
    implementation(libs.androidx.material3.adaptive)
    implementation(libs.androidx.material3.adaptive.navigation.suite)

    implementation(libs.timber)

    implementation(libs.org.slf4j.simple)

    testImplementation(libs.androidx.junit)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}