plugins {
    alias(libs.plugins.jellydroid.jvm.library)
}

dependencies {
    implementation(projects.core.model)

    implementation(libs.coroutines.core)
    implementation(libs.javax.inject)
    implementation(libs.kotlin.result)
    implementation(libs.kotlin.result.coroutines)

    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk)
}