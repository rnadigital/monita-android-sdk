package com.example

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.rnadigital.monita_android_sdk.Logger
import com.rnadigital.monita_android_sdk.MonitaSDK
import com.rnadigital.monita_android_sdk.SendToServer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Request
import kotlin.math.pow

class SendDataToServer {

    fun uploadHttpData(request: Request){
        if (MonitaSDK.isSDKInitialized()) {
            SendToServer().createHTTPMonitaData(request)
        } else {
            Logger().error("Intercepted MonitaSDK is not initialized. Unable to process the request.")

        }
    }


    fun uploadFirebaseData(fa: FirebaseAnalytics, name: String, params: Bundle){
        if (MonitaSDK.isSDKInitialized()) {
            SendToServer().createFirebaseMonitaData(fa, name , params)
        } else {
            Logger().error("Intercepted MonitaSDK is not initialized. Unable to process the request.")

        }
    }

    fun uploadFacebookData( name: String, params: Bundle){
        if (MonitaSDK.isSDKInitialized()) {
            SendToServer().createFacebookMonitaData(name , params)
        } else {
            Logger().error("Intercepted MonitaSDK is not initialized. Unable to process the request.")

        }
    }


//    fun uploadGoogleAdsData( name: String, params: Bundle){
//        if (MonitaSDK.isInitialized) {
//            SendToServer().createGoogleAdsMonitaData(name , params)
//        } else {
//            Logger().log("Intercepted MonitaSDK is not initialized. Unable to process the request.")
//
//        }
//    }


    fun uploadAdobeAnalyticsData( name: String, params: Bundle){
        if (MonitaSDK.isSDKInitialized()) {
            SendToServer().createAdobeAnalyticsMonitaData(name , params)
        } else {
            Logger().error("Intercepted MonitaSDK is not initialized. Unable to process the request.")

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
                isInitialized = MonitaSDK.isSDKInitialized() // Call the API to check initialization status

                if (isInitialized) {
                    // Proceed with the action once initialized
                    SendToServer().createGoogleAdsMonitaData(name, params)
                    return@launch
                } else {
                    // Log the status and increase the delay exponentially
                    Logger().error("MonitaSDK is not initialized. Retry attempt: $attempt")
                    val delayTime = baseDelay * 2.0.pow(attempt).toLong()
                    delay(delayTime)
                    attempt++
                }
            }

            // Handle the case when max retries are reached
            Logger().error("Intercepted: MonitaSDK could not be initialized after $maxRetries attempts. Unable to process the request.")
        }
    }



}