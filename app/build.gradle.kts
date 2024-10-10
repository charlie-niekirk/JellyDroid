plugins {
    alias(libs.plugins.jellydroid.android.application)
    alias(libs.plugins.jellydroid.android.application.compose)
    alias(libs.plugins.jellydroid.android.application.flavors)
    alias(libs.plugins.jellydroid.android.hilt)
    alias(libs.plugins.com.google.gms.google.services)
    alias(libs.plugins.com.google.firebase.crashlytics)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
}

android {
    namespace = "me.cniekirk.jellydroid"
    compileSdk = 35

    defaultConfig {
        applicationId = "me.cniekirk.jellydroid"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    ksp {
        arg("room.generateKotlin", "true")
    }
}

dependencies {
    api(projects.core.designsystem)

    api(projects.feature.onboarding)
    api(projects.feature.home)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.org.jetbrains.kotlinx.serialization)

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