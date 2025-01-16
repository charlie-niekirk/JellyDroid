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
    }
    gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))
    gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:clean"))
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
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
