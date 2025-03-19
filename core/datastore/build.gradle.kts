import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.jellydroid.android.library)
    alias(libs.plugins.jellydroid.android.hilt)
    alias(libs.plugins.com.google.protobuf)
}

android {
    namespace = "me.cniekirk.jellydroid.core.datastore"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
//    api(projects.core.common)
    implementation(libs.androidx.core.ktx)
    implementation(libs.datastore)
    implementation(libs.protobuf)

    testImplementation(libs.coroutines.test)
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
            }
        }
    }
}

androidComponents {
    onVariants(selector().all()) { variant ->
        afterEvaluate {
            val capName = variant.name
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            tasks.getByName<KotlinCompile>("ksp${capName}Kotlin") {
                setSource(tasks.getByName("generate${capName}Proto").outputs)
            }
        }
    }
}