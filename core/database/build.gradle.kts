plugins {
    alias(libs.plugins.jellydroid.android.library)
    alias(libs.plugins.jellydroid.android.hilt)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.parcelize)
}

android {
    namespace = "me.cniekirk.jellydroid.core.database"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
//    api(projects.core.common)

    implementation(libs.androidx.core.ktx)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    testImplementation(libs.coroutines.test)
    testImplementation(libs.room.testing)
}