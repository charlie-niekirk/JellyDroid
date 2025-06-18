pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven {
            // You can find the maven URL for other artifacts (e.g. KMP, METALAVA) on their
            // build pages.
            url = uri("https://androidx.dev/snapshots/builds/13647192/artifacts/repository")
        }
    }
    gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))
    gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:clean"))
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://androidx.dev/snapshots/builds/13647192/artifacts/repository")
        }
    }
}

rootProject.name = "JellyDroid"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")

include(":core:network")
include(":core:data")
include(":core:model")
include(":core:designsystem")
include(":feature:onboarding")
include(":core:analytics")
include(":core:database")
include(":core:datastore")
include(":core:common")
include(":core:domain")
include(":feature:home")
include(":feature:mediaDetails")
include(":core:test")
include(":core:player")
include(":feature:mediaPlayer")
include(":feature:mediaPlayer")
include(":feature:mediaCollection")
include(":feature:settings")
include(":core:navigation")
include(":feature:library")
