package com.example.monita_adapter_library.facebookLogs

import net.bytebuddy.asm.Advice
import android.os.Bundle
import com.example.SendDataToServer
import com.facebook.appevents.AppEventsLogger
import com.rnadigital.monita_android_sdk.Logger

object LogEventAdviceFb {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(
        @Advice.This logger: AppEventsLogger, // The instance of AppEventsLogger
        @Advice.Argument(0) eventName: String,
        @Advice.Argument(1) params: Bundle?
    ) {


        if (params != null) {
            SendDataToServer().uploadFacebookData(eventName, params)
        }

//        logger.logEvent(eventName,params)
        // Log the event name and the contents of the params bundle
        Logger().log("Intercepted Facebook logEvent: eventName=$eventName, params=${bundleToString(params)}")
    }


    // Helper function to convert Bundle to a readable string
    @JvmStatic
    public fun bundleToString(bundle: Bundle?): String {
        if (bundle == null) return "null"
        val keys = bundle.keySet()
        val stringBuilder = StringBuilder()
        for (key in keys) {
            stringBuilder.append(key).append("=").append(bundle.get(key)).append(", ")
        }
        return stringBuilder.toString().trimEnd(',', ' ')
    }
}




