package com.example

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.rnadigital.monita_android_sdk.MonitaSDK
import com.rnadigital.monita_android_sdk.SendToServer
import okhttp3.Request

class SendDataToServer {

    fun uploadHttpData(request: Request){
        if (MonitaSDK.isInitialized) {
            SendToServer().createHTTPMonitaData(request)
        } else {
            println("MonitaSDK is not initialized. Unable to process the request.")

        }
    }


    fun uploadFirebaseData(fa: FirebaseAnalytics, name: String, params: Bundle){
        if (MonitaSDK.isInitialized) {
            SendToServer().createFirebaseMonitaData(fa, name , params)
        } else {
            println("MonitaSDK is not initialized. Unable to process the request.")

        }
    }

    fun uploadFacebookData( name: String, params: Bundle){
        if (MonitaSDK.isInitialized) {
            SendToServer().createFacebookMonitaData(name , params)
        } else {
            println("MonitaSDK is not initialized. Unable to process the request.")

        }
    }



}