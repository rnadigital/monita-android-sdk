package com.rnadigital.monita_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.adobe.marketing.mobile.MobileCore
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.rnadigital.monita_android.ui.theme.Monita_androidTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException


class MainActivity : ComponentActivity() {

    private val token = "fe041147-0600-48ad-a04e-d3265becc4eb"
    private val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
    private var interstitialAd: AdManagerInterstitialAd? = null


    // Firebase Analytics instance
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    // Facebook App Events logger instance
    private lateinit var logger: AppEventsLogger
    val coroutineScope = CoroutineScope(Dispatchers.IO)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Firebase Analytics and Facebook App Events Logger
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        logger = AppEventsLogger.newLogger(this)

        // Log custom events for Firebase and Facebook
//        logCustomEventFB()
//        setUserProperty()
//        logFirebaseEvent(firebaseAnalytics)
//        testAdobeAnalytics()

        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@MainActivity) {}
            loadAd()
        }



        // Set up Compose UI
        setContent {
//            Monita_androidTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    CustomizableBackpackScreen()
//                }
//            }

            AnalyticsApp(
                onFirebaseAnalyticsClick = { logFirebaseEvent(firebaseAnalytics) },
                onFacebookAnalyticsClick = { logCustomEventFB() },
                onAdobeAnalyticsClick = { testAdobeAnalytics() },
                onGoogleAdsClick = { showAd() },
                onApiCallClick = { performApiCall() }
            )
        }
    }

    /**
     * Logs a custom event to Facebook Analytics.
     */
    private fun logCustomEventFB() {
        val params = Bundle().apply {
            putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "USD")
            putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product")
            putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, "HDFU-8452")
        }

        // Log the "Added to Cart" event with the specified parameters.
        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, params)

        logFirebaseEvent(firebaseAnalytics)
    }

    /**
     * Sets a user property for Firebase Analytics.
     */
    private fun setUserProperty() {
        firebaseAnalytics.setUserProperty("favorite_food", "Pizza")
    }

    /**
     * Logs an event to both Firebase and Facebook Analytics.
     */
    private fun logFirebaseEvent(fa: FirebaseAnalytics) {



        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, "id_123")
            putString(FirebaseAnalytics.Param.ITEM_NAME, "sample_item")
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, "content_type")
        }

        // Log the event with Facebook
//        logger.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

        // Log the event with Firebase
        fa.logEvent("TestEvent", bundle)

        println("logFirebaseEvent button clicked TestEvent =  $bundle ")
    }

    private fun loadAd() {
        runOnUiThread {


            val adRequest = AdManagerAdRequest.Builder().build()

            AdManagerInterstitialAd.load(
                this,
                "ca-app-pub-3940256099942544/1033173712",
                adRequest,
                object : AdManagerInterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: AdManagerInterstitialAd) {
                        interstitialAd = ad
                        println("Interstitial ad loaded.")
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        println("Failed to load interstitial ad: ${loadAdError.message}")
                    }
                }
            )

        }
    }

    private fun showAd() {
        interstitialAd?.show(this)
    }

    fun testAdobeAnalytics() {
        val contextData = mapOf(
            "pageName" to "HomeScreen",
            "screenOrientation" to "portrait"
        )

        // Call the trackState method, which should be intercepted
        MobileCore.trackState("HomeScreen", contextData)

        // Call the trackAction method, which should be intercepted
        MobileCore.trackAction("ButtonClicked", mapOf("button_name" to "SubscribeButton"))
    }


    private fun performApiCall() {

        coroutineScope.launch {
            sendPurchaseEvent(this@MainActivity)
        }



//        val client = OkHttpClient()
//        val request = Request.Builder()
//            .url("https://jsonplaceholder.typicode.com/posts")
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                println("HTTP call failed: ${e.message}")
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                response.body?.string()?.let {
//                    println("HTTP call response: $it")
//                }
//            }
//        })
    }


}

