package me.cniekirk.jellydroid

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("implementation", libs.findLibrary("androidx-ui").get())
            add("implementation", libs.findLibrary("androidx-ui-graphics").get())
            add("implementation", libs.findLibrary("androidx-ui-tooling").get())
            add("implementation", libs.findLibrary("androidx-ui-tooling-preview").get())
            add("implementation", libs.findLibrary("androidx-material3").get())


            add("androidTestImplementation", platform(bom))
            // Add ComponentActivity to debug manifest
            add("debugImplementation", libs.findLibrary("androidx-ui-test-manifest").get())
        }
    }
}