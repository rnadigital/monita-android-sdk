pluginManagement {
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
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"

}
//dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
//    repositories {
//        google()
//        mavenCentral()
//        maven { url = uri("https://jitpack.io")  }
//
//    }
//}
//
//rootProject.name = "Monita_android"
//include(":app")
//include(":monita-android-sdk")
//include(":monita-adapter-library")


dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":monita-android-sdk", ":monita-adapter-library")

