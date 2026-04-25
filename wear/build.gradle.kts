plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.alvaroquintana.edadperruna.wear"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.alvaroquintana.edadperruna"
        minSdk = 30
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Pure JVM domain shared with :core (no Android, no Hilt — keeps Wear cold-start fast)
    implementation(project(":core-domain-pure"))

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.activity)
    debugImplementation(libs.compose.ui.tooling)

    // Wear Compose
    implementation(libs.wear.compose.foundation)
    implementation(libs.wear.compose.material3)
    implementation(libs.wear.compose.navigation)
    implementation(libs.wear.tooling.preview)

    // Wear Tiles + ProtoLayout — declarative UI rendered by SysUI on the watch face,
    // independent of the launcher Activity. Material3 helpers + dynamic-data
    // expressions support live timeline updates without recreating the tile.
    implementation(libs.wear.tiles)
    implementation(libs.wear.protolayout)
    implementation(libs.wear.protolayout.material3)
    implementation(libs.wear.protolayout.expression)
    implementation(libs.wear.tiles.tooling.preview)
    debugImplementation(libs.wear.tiles.tooling)
    // Guava is required at compile time for Futures.immediateFuture used by
    // TileService callbacks. wear-tiles only ships listenablefuture (the empty
    // stub) on its compile classpath; the real guava-android lives in runtime.
    implementation(libs.guava.android)

    // AndroidX
    implementation(libs.core.ktx)

    // Wear OS data sync
    implementation(libs.play.services.wearable)
    implementation(libs.coroutines.play.services)
}
