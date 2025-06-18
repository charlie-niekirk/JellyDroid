import com.android.build.api.dsl.ApplicationExtension
import me.cniekirk.jellydroid.configureAndroidCompose
import me.cniekirk.jellydroid.extensions.ComposeConventionExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")

            val conventionExtension = ComposeConventionExtension.register(project)
            val extension = extensions.getByType<ApplicationExtension>()

            configureAndroidCompose(extension, conventionExtension)
        }
    }

}