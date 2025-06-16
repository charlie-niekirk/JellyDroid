import com.android.build.api.dsl.ApplicationExtension
import me.cniekirk.jellydroid.configureKotlinAndroid
import me.cniekirk.jellydroid.configureSigning
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                configureSigning(this)
                defaultConfig.targetSdk = 36
            }
        }
    }
}