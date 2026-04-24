pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "edadperruna"
include(":app")
include(":core")
include(":core-domain-pure")
include(":core-designsystem")
include(":benchmark")
include(":widget")
include(":wear")
