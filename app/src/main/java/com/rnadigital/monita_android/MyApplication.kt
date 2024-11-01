package com.rnadigital.monita_android

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import com.adobe.marketing.mobile.Analytics
import com.adobe.marketing.mobile.Extension
import com.adobe.marketing.mobile.LoggingMode
import com.adobe.marketing.mobile.MobileCore
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.rnadigital.monita_android_sdk.Logger
import com.rnadigital.monita_android_sdk.MonitaSDK


class MyApplication : Application() {
    val token = "fe041147-0600-48ad-a04e-d3265becc4eb"

    private val ENVIRONMENT_FILE_ID = "3149c49c3910/b6541e5e6301/launch-f7ac0a320fb3-development"


    override fun onCreate() {
        super.onCreate()

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        MonitaSDK.Builder(this)
            .enableLogger(false) // Enable logging
            .setToken(token) // Set the token
            .build {
                // Callback when initialization is complete
                // You can place any setup code here
            }

        FacebookSdk.sdkInitialize(this)
        AppEventsLogger.activateApp(this)


        // Initialize Adobe Mobile SDK Core
        MobileCore.setApplication(this)
        MobileCore.configureWithAppID(ENVIRONMENT_FILE_ID);

//        MobileCore.setLogLevel(LoggingMode.DEBUG)

        // Register Adobe extensions
        try {
            MobileCore.registerExtensions(listOf(Analytics::class.java as Class<out Extension>)) {
                Log.d("android App","Adobe Analytics extension registered successfully.")
            }
        } catch (e: Exception) {
            Log.d("android App","Error registering Adobe Analytics extension: ${e.message}")
        }
    }
}