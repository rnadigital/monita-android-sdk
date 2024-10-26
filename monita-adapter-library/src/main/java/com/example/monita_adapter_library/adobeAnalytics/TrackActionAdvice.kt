package com.example.monita_adapter_library.adobeAnalytics

import net.bytebuddy.asm.Advice
import android.os.Bundle
import com.example.SendDataToServer

object TrackActionAdvice {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(
        @Advice.Argument(0) action: String, // The action name
        @Advice.Argument(1) contextData: Map<String, Any>?
    ) {
        // Log the action being tracked
        println("Intercepted Adobe Analytics trackAction: action=$action")
        println("Intercepted Adobe Analytics Context data: ${contextData?.toString() ?: "No context data"}")

        // Convert contextData to Bundle
        val dataBundle = Bundle().apply {
            putString("action", action)
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

            // Add a custom parameter, if needed
            putString("user_id", "user123")
        }

        // Log the modified context data for debugging
        println("Modified context data in Bundle: $dataBundle")

        // Send data to server
        SendDataToServer().uploadAdobeAnalyticsData("track_action_event", dataBundle)


    }
}
