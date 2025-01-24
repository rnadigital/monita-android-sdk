plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("maven-publish")
    id("kotlin-kapt")

}

android {
    namespace = "com.rnadigital.monita_android_sdk"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        version = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

//publishing {
//    publications {
//        register<MavenPublication>("release") {
//            afterEvaluate {
//                from(components["release"])
//            }
//        }
//    }
//}

publishing {
    publications {
        register<MavenPublication>("release") {
            afterEvaluate {
                from(components.findByName("release") ?: throw GradleException("Release component not found"))
                groupId = "com.github.rnadigital"
                artifactId = "monita-android-sdk"
                version = "1.6.0"
            }
        }
    }
}



dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.play.services.measurement.api)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation (libs.okhttp) // or latest version
    implementation (libs.gson) // Use the latest version if available
    implementation (libs.androidx.work.runtime.ktx)
    implementation (libs.androidx.room.ktx)
    // Room dependencies
    implementation (libs.androidx.room.runtime)
    kapt ("androidx.room:room-compiler:2.6.1")





//    implementation (libs.firebase.analytics.ktx)



}