plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.example.monita_adapter_library"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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
                artifactId = "monita-adapter-library"
                version = "1.8.0"
            }
        }
    }
}


dependencies {

    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.play.services.measurement.api)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.byte.buddy)
    implementation(libs.byte.buddy.android)
    implementation(libs.okhttp)
    implementation (libs.facebook.marketing)
    implementation (libs.play.services.ads)
    implementation (libs.core)
    implementation (libs.marketing)
    implementation(project(":monita-android-sdk"))

}