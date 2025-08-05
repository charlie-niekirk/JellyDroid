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