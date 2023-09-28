pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        jcenter()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven (url = {
            "https://jitpack.io"
        })
        maven( url = { "https://sdk.squareup.com/public/android" })
    }
}

rootProject.name = "TipTop"
include(":app")
