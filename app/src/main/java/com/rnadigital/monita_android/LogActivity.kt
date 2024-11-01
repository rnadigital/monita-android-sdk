package com.rnadigital.monita_android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.rnadigital.monita_android.ui.theme.Monita_androidTheme
import com.rnadigital.monita_android_sdk.Logger
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException

class LogActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Analytics
        firebaseAnalytics = Firebase.analytics

        setContent {
            AnalyticsApp(
                onFirebaseAnalyticsClick = { logFirebaseEvent() },
                onFacebookAnalyticsClick = { logFacebookEvent() },
                onAdobeAnalyticsClick = { logAdobeEvent() },
                onGoogleAdsClick = { loadGoogleAds() },
                onApiCallClick = { performApiCall() }
            )
        }
    }

    private fun logFirebaseEvent() {
        val bundle = Bundle().apply { putString("event_name", "firebase_event") }
//        firebaseAnalytics.logEvent("firebase_analytics", bundle)
    }

    private fun logFacebookEvent() {
        // Facebook analytics logic goes here (replace with actual implementation)
        // FacebookSdk.sdkInitialize(applicationContext)
        Log.d("android App", "Facebook analytics event logged.")
    }

    private fun logAdobeEvent() {
        // Adobe analytics logic goes here (replace with actual implementation)
        Log.d("android App","Adobe analytics event logged.")
    }

    private fun loadGoogleAds() {
        // Google AdMob logic goes here (replace with actual implementation)
        Log.d("android App","Google Ad loaded.")
    }

    private fun performApiCall() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://jsonplaceholder.typicode.com/posts")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("android App","HTTP call failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let {
                    Log.d("android App","HTTP call response: $it")
                }
            }
        })
    }
}
