plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.alvaroquintana.edadperruna.widget"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(project(":core-designsystem"))

    // Compose — needed to reference ColorScheme exposed by :core-designsystem
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material3)

    // Glance (Compose-flavored widgets)
    implementation(libs.glance.appwidget)
    implementation(libs.glance.material3)

    // AndroidX
    implementation(libs.core.ktx)
}
