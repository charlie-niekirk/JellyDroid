import com.android.build.gradle.LibraryExtension
import me.cniekirk.jellydroid.configureAndroidCompose
import me.cniekirk.jellydroid.extensions.ComposeConventionExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("jellydroid.android.library")

            val conventionExtension = ComposeConventionExtension.register(this)
            val extension = extensions.getByType<LibraryExtension>()

            configureAndroidCompose(extension, conventionExtension)
        }
    }
}