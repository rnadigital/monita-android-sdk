package com.example.monita_adapter_library.adobeAnalytics

import net.bytebuddy.asm.Advice
import android.os.Bundle
import com.example.SendDataToServer
import com.rnadigital.monita_android_sdk.Logger
import org.json.JSONObject


object TrackStateAdvice {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(
        @Advice.Argument(0) state: String, // The state name or screen name
        @Advice.Argument(1) contextData: Map<String, Any>?
    ) {
        // Log the state being tracked
        Logger().log("Intercepted Adobe Analytics trackState: state=$state")
        Logger().log("Intercepted Adobe Analytics Context data: ${contextData?.toString() ?: "No context data"}")

        // Convert contextData to Bundle
        val dataBundle = Bundle().apply {
            putString("state", state)
            contextData?.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Float -> putFloat(key, value)
                    is Double -> putDouble(key, value)
                    is Long -> putLong(key, value)
                    else -> putString(key, value.toString())
                }
            }

            // Add a custom parameter if needed
            putString("custom_parameter", "custom_value")
        }

        // Log the modified context data for debugging
        Logger().log("Modified context data in Bundle: $dataBundle")

        // Send data to server
        SendDataToServer().uploadAdobeAnalyticsData("track_state_event", dataBundle)
    }
}

