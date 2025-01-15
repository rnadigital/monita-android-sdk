plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    id("net.bytebuddy.byte-buddy-gradle-plugin") version "1.15.5"
    alias(libs.plugins.kotlin.compose) // Use the latest version

}

android {
    namespace = "com.rnadigital.monita_android"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rnadigital.monita_android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
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

    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation (libs.okhttp)

    implementation (libs.material3)
    implementation (libs.byte.buddy.android)
    implementation (libs.play.services.ads)
    implementation (libs.androidx.navigation.compose)

    implementation(project(":monita-android-sdk"))
    byteBuddy (project(":monita-adapter-library"))
    implementation(project(":monita-adapter-library"))
    implementation (libs.facebook.marketing)
    implementation(libs.sdk.bom)
    implementation(libs.mobile.core)
    implementation(libs.analytics)
    implementation (libs.coil.compose)

//    implementation (libs.monita.android.sdk)


}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

