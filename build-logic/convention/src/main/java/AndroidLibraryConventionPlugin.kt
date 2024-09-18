import com.android.build.gradle.LibraryExtension
import me.cniekirk.jellydroid.configureFlavors
import me.cniekirk.jellydroid.configureKotlinAndroid
import me.cniekirk.jellydroid.libs
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
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 35
                configureFlavors(this)
            }
            dependencies {
                add("implementation", libs.findLibrary("timber").get())
                add("testImplementation", kotlin("test"))
//                add("testImplementation", project(":core:testing"))
                add("androidTestImplementation", kotlin("test"))
//                add("androidTestImplementation", project(":core:testing"))
            }
        }
    }
}