plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.gms.google.services)
}

android {
    namespace = "com.example.foodallergenfinal"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.foodallergenfinal"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
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
    viewBinding {
        enable = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Navigation Component (Check if available in Version Catalog)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // firebase auth
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)

    // coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // view model
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.fragment)

    // ml-kit barcode
    implementation(libs.ml.barcode)

    // ml kit ocr
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.1")

    // camera x
    implementation(libs.camera.core)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)
    implementation("androidx.camera:camera-camera2:1.3.0")

    // glide
    implementation (libs.glide)
}