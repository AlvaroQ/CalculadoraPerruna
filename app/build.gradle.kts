import javax.xml.parsers.DocumentBuilderFactory

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.compose.screenshot)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.kover)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.alvaroquintana.edadperruna"
    compileSdk = libs.versions.compileSdk.get().toInt()

    packaging {
        resources {
            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }

    signingConfigs {
        create("config") {
            keyAlias = findProperty("EDADPERRUNA_RELEASE_KEY_ALIAS") as String?
            keyPassword = findProperty("EDADPERRUNA_RELEASE_KEY_PASSWORD") as String?
            storeFile = findProperty("EDADPERRUNA_RELEASE_STORE_FILE")?.let { file(it) }
            storePassword = findProperty("EDADPERRUNA_RELEASE_STORE_PASSWORD") as String?
        }
    }

    defaultConfig {
        applicationId = "com.alvaroquintana.edadperruna"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 520
        versionName = "5.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isJniDebuggable = true
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false

            resValue("string", "BANNER_MAIN", getAdmobId("admob_banner_test_id"))
            resValue("string", "BANNER_LIST", getAdmobId("admob_banner_test_id"))
            resValue("string", "INTERSTICIAL_RESULT", getAdmobId("admob_intersticial_list_id"))
            resValue("string", "BONIFICADO_LIST", getAdmobId("admob_bonificado_test_id"))
            resValue("string", "BANNER_PREFERENCES", getAdmobId("admob_banner_test_id"))
            resValue("string", "BANNER_DESCRIPTION", getAdmobId("admob_banner_test_id"))

            buildConfigField("Boolean", "uploadBreedsFromJSON", "true")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            isDebuggable = false
            signingConfig = signingConfigs.getByName("config")

            resValue("string", "BANNER_MAIN", getAdmobId("admob_banner_main_id"))
            resValue("string", "BANNER_LIST", getAdmobId("admob_banner_list_id"))
            resValue("string", "INTERSTICIAL_RESULT", getAdmobId("admob_intersticial_result_id"))
            resValue("string", "BONIFICADO_LIST", getAdmobId("admob_bonificado_list_id"))
            resValue("string", "BANNER_PREFERENCES", getAdmobId("admob_banner_settings_id"))
            resValue("string", "BANNER_DESCRIPTION", getAdmobId("admob_banner_description_id"))

            buildConfigField("Boolean", "uploadBreedsFromJSON", "false")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
        compose = true
        resValues = true
    }

    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core-designsystem"))
    implementation(project(":widget"))

    // Baseline Profiles runtime
    implementation(libs.profileinstaller)
    baselineProfile(project(":benchmark"))

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons)
    implementation(libs.compose.activity)
    implementation(libs.compose.navigation)
    implementation(libs.compose.lifecycle)
    implementation(libs.compose.viewmodel)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    // Coil
    implementation(libs.coil.compose)

    // Vico Charts
    implementation(libs.vico.compose.m3)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database)
    implementation(libs.listenablefuture)

    // AndroidX
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.material)
    implementation(libs.splash.screen)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Ads
    implementation(libs.play.services.ads)

    // Wear OS data sync (companion channel)
    implementation(libs.play.services.wearable)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.coroutines.test)

    // Screenshot testing (Compose Preview Screenshot Testing — official Google)
    screenshotTestImplementation(libs.compose.ui.tooling)
    screenshotTestImplementation(libs.compose.screenshot.validation)

    // Architecture tests
    testImplementation(libs.konsist)
}

configurations.configureEach {
    exclude(group = "com.google.android.gms", module = "play-services-safetynet")
}

fun getAdmobId(keyName: String): String {
    return try {
        val secretsFile = file("./src/main/res/values/secrets.xml")
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(secretsFile)
        val nodes = doc.getElementsByTagName("string")
        for (i in 0 until nodes.length) {
            val node = nodes.item(i)
            if (node.attributes.getNamedItem("name").nodeValue == keyName) {
                return node.textContent
            }
        }
        ""
    } catch (e: Exception) {
        println(e)
        ""
    }
}
