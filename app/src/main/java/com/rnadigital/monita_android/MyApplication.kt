package com.rnadigital.monita_android

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import com.adobe.marketing.mobile.Analytics
import com.adobe.marketing.mobile.Extension
import com.adobe.marketing.mobile.LoggingMode
import com.adobe.marketing.mobile.MobileCore
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.rnadigital.monita_android_sdk.MonitaSDK


class MyApplication : Application() {
    val token = "fe041147-0600-48ad-a04e-d3265becc4eb"
//    private val ENVIRONMENT_FILE_ID = "YOUR_APP_ENVIRONMENT_ID"


    override fun onCreate() {
        super.onCreate()

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        MonitaSDK.init (token){}

        FacebookSdk.sdkInitialize(this)
        AppEventsLogger.activateApp(this)


        // Initialize Adobe Mobile SDK Core
        MobileCore.setApplication(this)
//        MobileCore.configureWithAppID(ENVIRONMENT_FILE_ID);

        MobileCore.setLogLevel(LoggingMode.DEBUG)

        // Register Adobe extensions
        try {
            MobileCore.registerExtensions(listOf(Analytics::class.java as Class<out Extension>)) {
            println("Adobe Analytics extension registered successfully.")
            }
        } catch (e: Exception) {
            println("Error registering Adobe Analytics extension: ${e.message}")
        }
    }
}