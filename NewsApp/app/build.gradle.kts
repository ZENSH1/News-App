plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.screenshot)
}

android {
    namespace = "com.xs.newsapp"
    compileSdk = 37

    defaultConfig {
        // The app's permanent identity on devices, Google Play and Firebase (it must match the package
        // registered in google-services.json), so it keeps its original value. To change it, register
        // the new package in the Firebase console and replace app/google-services.json.
        applicationId = "com.xs.firenotesapp"
        minSdk = 28
        targetSdk = 37
        versionCode = 2
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Two backends behind the same repositories. Pick one with the "Build Variants" panel.
    //  - mock: in-memory fake backend with demo data (default). No Firebase project needed.
    //  - prod: Firebase Authentication + Cloud Firestore (uses app/google-services.json).
    flavorDimensions += "backend"
    productFlavors {
        create("mock") {
            dimension = "backend"
            isDefault = true
        }
        create("prod") {
            dimension = "backend"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    testOptions {
        // Lets Robolectric-backed Compose UI tests in src/test read app resources.
        unitTests.isIncludeAndroidResources = true
    }

    // Screenshot tests: previews in src/screenshotTest, rendered on the JVM (see README).
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.coroutines.android)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.animation.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // MVI stores are ViewModels; collect their state lifecycle-aware
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Navigation 3 (+ per-destination ViewModels), keys are @Serializable
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.kotlinx.serialization.core)

    // Dependency injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)

    // Firebase backend, only compiled into the `prod` flavor.
    "prodImplementation"(platform(libs.firebase.bom))
    "prodImplementation"(libs.firebase.auth)
    "prodImplementation"(libs.firebase.firestore)
    "prodImplementation"(libs.kotlinx.coroutines.play.services)

    // Local (JVM) tests: reducers, stores, navigation, and Robolectric-backed screen tests.
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.androidx.compose.ui.test.junit4)

    // Screenshot tests (Compose Preview Screenshot Testing)
    screenshotTestImplementation(libs.screenshot.validation.api)
    screenshotTestImplementation(libs.androidx.compose.ui.tooling)
}
