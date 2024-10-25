package com.example.monita_adapter_library.adobeAnalytics

import net.bytebuddy.asm.Advice
import android.os.Bundle
import org.json.JSONObject

object TrackStateAdvice {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(
        @Advice.Argument(0) state: String, // The state name or screen name
        @Advice.Argument(1) contextData: Map<String, Any>?
    ) {
        // Log the state being tracked
        println("Intercepted Adobe Analytics trackState: state=$state")
        println("Context data: ${contextData?.toString() ?: "No context data"}")

        // Modify the context data if needed
        if (contextData != null) {
            // Example: Add a custom parameter
            val mutableContextData = contextData.toMutableMap()
            mutableContextData["custom_parameter"] = "custom_value"
            println("Modified context data: $mutableContextData")
        }
    }
}
