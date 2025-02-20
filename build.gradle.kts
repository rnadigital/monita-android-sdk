// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.google.firebase.crashlytics) apply false
    alias(libs.plugins.kotlin.compose) apply false


}

//
//plugins {
//    kotlin("android") version "1.8.20" apply false
//    id("com.android.application") apply false
//}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}


//buildscript {
//    repositories {
//        google()
//        mavenCentral()
//    }
//    dependencies {
//        classpath (libs.gradle)
//        classpath (libs.byte.buddy.gradle.plugin)
//
//    }
//}
