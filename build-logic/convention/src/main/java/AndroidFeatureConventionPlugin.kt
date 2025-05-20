import com.android.build.api.dsl.LibraryExtension
import me.cniekirk.jellydroid.androidTestImplementation
import me.cniekirk.jellydroid.implementation
import me.cniekirk.jellydroid.libs
import me.cniekirk.jellydroid.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("jellydroid.android.library")
                apply("jellydroid.android.hilt")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            extensions.configure<LibraryExtension> {
                @Suppress("UnstableApiUsage")
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                    }
                }
            }

            dependencies {
                implementation(projects.core.designsystem)
                implementation(projects.core.common)
                implementation(projects.core.domain)
                implementation(projects.core.analytics)

                implementation(libs.findLibrary("hilt.navigation").get())
                implementation(libs.findLibrary("androidx.navigation.compose").get())
                implementation(libs.findLibrary("androidx.animation").get())

                implementation(libs.findLibrary("org.jetbrains.kotlinx.serialization").get())

                implementation(libs.findLibrary("orbit.core").get())
                implementation(libs.findLibrary("orbit.compose").get())
                implementation(libs.findLibrary("orbit.viewmodel").get())

                implementation(libs.findLibrary("kotlin.result").get())
                implementation(libs.findLibrary("kotlin.result.coroutines").get())

                testImplementation(kotlin("test"))
                testImplementation(projects.core.test)

                testImplementation(libs.findLibrary("orbit.test").get())
                testImplementation(libs.findLibrary("mockk").get())
                testImplementation(libs.findLibrary("androidx.navigation.testing").get())
                testImplementation(libs.findLibrary("androidx.junit.ext").get())
                testImplementation(libs.findLibrary("robolectric").get())

                androidTestImplementation(kotlin("test"))
            }
        }
    }
}