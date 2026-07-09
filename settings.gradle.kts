pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("androidx.*")
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            content {
                includeGroupByRegex("androidx.*")
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "micro-apps"
include(":core")
include(":app-shift")
// include(":app-anchor")  // Added when built
// include(":app-commit")  // Added when built
// include(":app-pause")   // Added when built

gradle.beforeProject {
    val buildDirName = if (path == ":") "root" else path.replace(":", "_")
    layout.buildDirectory.set(file("C:/Users/damia/AppData/Local/Temp/gradle-build/micro-apps/$buildDirName"))
}
