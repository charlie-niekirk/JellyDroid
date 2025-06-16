import com.android.build.gradle.LibraryExtension
import me.cniekirk.jellydroid.androidTestImplementation
import me.cniekirk.jellydroid.configureKotlinAndroid
import me.cniekirk.jellydroid.implementation
import me.cniekirk.jellydroid.libs
import me.cniekirk.jellydroid.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("jellydroid.android.detekt")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 36
            }
            dependencies {
                implementation(libs.findLibrary("timber").get())
                testImplementation(kotlin("test"))
                androidTestImplementation(kotlin("test"))
            }
        }
    }
}