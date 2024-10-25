package com.example.monita_adapter_library.adobeAnalytics

import net.bytebuddy.asm.Advice
import android.os.Bundle

object TrackActionAdvice {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(
        @Advice.Argument(0) action: String, // The action name
        @Advice.Argument(1) contextData: Map<String, Any>?
    ) {
        // Log the action being tracked
        println("Intercepted Adobe Analytics trackAction: action=$action")
        println("Context data: ${contextData?.toString() ?: "No context data"}")

        // Modify the context data if needed
        if (contextData != null) {
            // Example: Add a custom parameter
            val mutableContextData = contextData.toMutableMap()
            mutableContextData["user_id"] = "user123"
            println("Modified context data: $mutableContextData")
        }
    }
}
