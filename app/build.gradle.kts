import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "ch.nevis.mobile.authentication.fido2.example"
    compileSdk = 35

    defaultConfig {
        applicationId = "ch.nevis.mobile.authentication.fido2.example"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        all {
            signingConfig = signingConfigs.getByName("debug") {
                // Define the signing config, uncomment these lines
                // storeFile = file(getConfig("KEYSTORE_FILE"))
                // storePassword = getConfig("KEYSTORE_PASSWORD")
                // keyAlias = getConfig("KEY_ALIAS")
                // keyPassword = getConfig("KEY_PASSWORD")
            }

            // HOST_NAME and BACKEND_ACCESS_TOKEN are injected from local.properties or environment variables
            // (see getConfig() at the bottom of this file) — never hardcoded in source.
            buildConfigField("String", "BaseUrl", "\"https://${getConfig("HOST_NAME")}\"")
            buildConfigField("String", "AccessToken", "\"${getConfig("BACKEND_ACCESS_TOKEN")}\"")
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.credentials)
    // Needed for credentials support from play services, for devices running
    // Android 13 and below.
    implementation(libs.androidx.credentials.play.services.auth)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Chrome Tabs
    implementation(libs.androidx.browser)

    // JSON serialization library, works with the Kotlin serialization plugin
    implementation(libs.kotlinx.serialization.json)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    kapt(libs.hilt.compiler)
}

fun Project.getConfig(name: String): String {
    val localProperties = gradleLocalProperties(rootDir, providers)
    if (localProperties.containsKey(name)) {
        return localProperties.getProperty(name)
    }
    val env = System.getenv(name)
    if (env != null) {
        return env
    }
    val prop = System.getProperty(name)
    if (prop != null) {
        return prop
    }
    if (hasProperty(name)) {
        return property(name).toString()
    }

    throw GradleException("Getting configuration with name $name failed! Set it as environment variable or as local/project/system property.")
}
