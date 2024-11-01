package com.example.monita_adapter_library.firebase_analytics

import android.app.Activity
import net.bytebuddy.asm.Advice
import android.os.Bundle
import com.example.SendDataToServer
import com.google.firebase.analytics.FirebaseAnalytics
import com.rnadigital.monita_android_sdk.Logger


object LogEventAdvice {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(
        @Advice.This fa: FirebaseAnalytics, // Use @Advice.This to refer to the instance
        @Advice.Argument(0) name: String,
        @Advice.Argument(1) params: Bundle
    ) {
        // Log the event name and the contents of the params bundle
        Logger().log("Intercepted Firebase logEvent: name=$name, params=${bundleToString(params)}")
        SendDataToServer().uploadFirebaseData(fa, name, params)
//        fa.logEvent(name,params)
    }

    @JvmStatic
    public fun bundleToString(bundle: Bundle): String {
        val keys = bundle.keySet()
        val stringBuilder = StringBuilder()
        for (key in keys) {
            stringBuilder.append(key).append("=").append(bundle.get(key)).append(", ")
        }
        return stringBuilder.toString().trimEnd(',', ' ')
    }
}



object SetDefaultEventParametersAdvice {
    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(
        @Advice.This fa: FirebaseAnalytics,
        @Advice.Argument(0) parameters: Bundle?
    ) {
        if (parameters != null) {
            SendDataToServer().uploadFirebaseData(fa, "SetDefaultEventParameters", parameters)
        }


        // Log or handle setDefaultEventParameters call
        Logger().log("Intercepted Firebase setDefaultEventParameters: parameters=$parameters")
//        fa.setDefaultEventParameters(parameters)
    }
}


object SetCurrentScreenAdvice {
    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(
        @Advice.This fa: FirebaseAnalytics,
        @Advice.Argument(0) activity: Activity,
        @Advice.Argument(1) screenName: String?,
        @Advice.Argument(2) screenClassOverride: String?
    ) {
        // Log or handle setCurrentScreen call
        Logger().log("Intercepted Firebase setCurrentScreen: activity=${activity.localClassName}, screenName=$screenName, screenClassOverride=$screenClassOverride")
//    fa.setCurrentScreen(activity,screenName,screenClassOverride)
    }
}


object SetUserIdAdvice {
    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(
        @Advice.This fa: FirebaseAnalytics,
        @Advice.Argument(0) id: String
    ) {
        // Log or handle setUserId call
        Logger().log("Intercepted Firebase setUserId: id=$id")
//        fa.setUserId(id)
    }
}


object SetUserPropertyAdvice {
    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(
        @Advice.This fa: FirebaseAnalytics,
        @Advice.Argument(0) name: String,
        @Advice.Argument(1) value: String?
    ) {
        // Log or handle setUserProperty call
        Logger().log("Intercepted Firebase setUserProperty: name=$name, value=$value")
//        fa.setUserProperty(name,value)
    }
}


object SetAnalyticsCollectionEnabledAdvice {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(
        @Advice.This fa: FirebaseAnalytics, // The instance of FirebaseAnalytics
        @Advice.Argument(0) enabled: Boolean // The first and only argument: 'enabled'
    ) {
        // Log or handle setAnalyticsCollectionEnabled call
        Logger().log("Intercepted Firebase setAnalyticsCollectionEnabled: enabled=$enabled")
//        fa.setAnalyticsCollectionEnabled(enabled)
    }
}

