package digital.rna.monita_android_sdk.firebase

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseAnalyticsInterceptor(private val context: Context) {

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    // This function intercepts the event and forwards it to Firebase
    fun logEvent(eventName: String, params: Bundle) {
        // Intercept the event before sending it to Firebase
        interceptEvent(eventName, params)

        // Now forward the event to Firebase Analytics
        firebaseAnalytics.logEvent(eventName, params)
    }

    // Intercept the event and send it to the custom tracking server
    private fun interceptEvent(eventName: String, params: Bundle) {
        // Log event interception (could send this to a custom tracking server)
        Log.d("TrackingPlanSDK", "Intercepting event: $eventName with params: $params")

        // Simulate sending event to a custom server
        sendToCustomServer(eventName, params)
    }

    // Simulate sending the event data to a custom server
    private fun sendToCustomServer(eventName: String, params: Bundle) {
        // Here you would make a network call to send the event to your server
        Log.d("TrackingPlanSDK", "Sending event to custom server: $eventName")
        // Example: Use OkHttpClient or Retrofit to send the event to a custom backend
    }
}
