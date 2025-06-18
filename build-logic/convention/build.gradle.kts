import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    alias(libs.plugins.co.hinge.gradle.project.accessors)
}

group = "me.cniekirk.jellydroid.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.firebase.crashlytics.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
    implementation(libs.detekt.gradlePlugin)
    implementation(libs.kotlin.compose.compiler.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "jellydroid.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidFirebase") {
            id = "jellydroid.android.firebase"
            implementationClass = "AndroidApplicationFirebaseConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "jellydroid.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "jellydroid.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidLibrary") {
            id = "jellydroid.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "jellydroid.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidRoom") {
            id = "jellydroid.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidTest") {
            id = "jellydroid.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("androidHilt") {
            id = "jellydroid.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidDetekt") {
            id = "jellydroid.android.detekt"
            implementationClass = "AndroidDetektConventionPlugin"
        }
        register("jvmLibrary") {
            id = "jellydroid.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}