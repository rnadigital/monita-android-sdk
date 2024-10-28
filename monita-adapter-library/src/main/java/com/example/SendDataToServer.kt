package com.example

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.rnadigital.monita_android_sdk.MonitaSDK
import com.rnadigital.monita_android_sdk.SendToServer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Request
import kotlin.math.pow

class SendDataToServer {

    fun uploadHttpData(request: Request){
        if (MonitaSDK.isInitialized) {
            SendToServer().createHTTPMonitaData(request)
        } else {
            println("Intercepted MonitaSDK is not initialized. Unable to process the request.")

        }
    }


    fun uploadFirebaseData(fa: FirebaseAnalytics, name: String, params: Bundle){
        if (MonitaSDK.isInitialized) {
            SendToServer().createFirebaseMonitaData(fa, name , params)
        } else {
            println("Intercepted MonitaSDK is not initialized. Unable to process the request.")

        }
    }

    fun uploadFacebookData( name: String, params: Bundle){
        if (MonitaSDK.isInitialized) {
            SendToServer().createFacebookMonitaData(name , params)
        } else {
            println("Intercepted MonitaSDK is not initialized. Unable to process the request.")

        }
    }


//    fun uploadGoogleAdsData( name: String, params: Bundle){
//        if (MonitaSDK.isInitialized) {
//            SendToServer().createGoogleAdsMonitaData(name , params)
//        } else {
//            println("Intercepted MonitaSDK is not initialized. Unable to process the request.")
//
//        }
//    }


    fun uploadAdobeAnalyticsData( name: String, params: Bundle){
        if (MonitaSDK.isInitialized) {
            SendToServer().createAdobeAnalyticsMonitaData(name , params)
        } else {
            println("Intercepted MonitaSDK is not initialized. Unable to process the request.")

        }
    }


    fun uploadGoogleAdsData(name: String, params: Bundle) {
        GlobalScope.launch {
            val maxRetries = 5      // Max attempts to check initialization
            val baseDelay = 1000L   // Initial delay in milliseconds

            var attempt = 0
            var isInitialized = false

            // Retry with exponential backoff
            while (attempt < maxRetries) {
                isInitialized = MonitaSDK.isInitialized // Call the API to check initialization status

                if (isInitialized) {
                    // Proceed with the action once initialized
                    SendToServer().createGoogleAdsMonitaData(name, params)
                    return@launch
                } else {
                    // Log the status and increase the delay exponentially
                    println("MonitaSDK is not initialized. Retry attempt: $attempt")
                    val delayTime = baseDelay * 2.0.pow(attempt).toLong()
                    delay(delayTime)
                    attempt++
                }
            }

            // Handle the case when max retries are reached
            println("Intercepted: MonitaSDK could not be initialized after $maxRetries attempts. Unable to process the request.")
        }
    }



}