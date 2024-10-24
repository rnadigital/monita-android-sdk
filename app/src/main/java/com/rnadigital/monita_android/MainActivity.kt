package com.rnadigital.monita_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.rnadigital.monita_android.ui.theme.Monita_androidTheme
import okhttp3.MediaType.Companion.toMediaTypeOrNull



class MainActivity : ComponentActivity() {

    private val token = "fe041147-0600-48ad-a04e-d3265becc4eb"
    private val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
    private var interstitialAd: AdManagerInterstitialAd? = null


    // Firebase Analytics instance
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    // Facebook App Events logger instance
    private lateinit var logger: AppEventsLogger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Firebase Analytics and Facebook App Events Logger
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        logger = AppEventsLogger.newLogger(this)

        // Log custom events for Firebase and Facebook
        logCustomEventFB()
        setUserProperty()
        logFirebaseEvent(firebaseAnalytics)
        MobileAds.initialize(this) {}


        // Set up Compose UI
        setContent {
            Monita_androidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CustomizableBackpackScreen()
                }
            }
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
        logger.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

        // Log the event with Firebase
        fa.logEvent("TestEvent", bundle)
    }

    private fun loadAd() {
        val adRequest = AdManagerAdRequest.Builder().build()

        AdManagerInterstitialAd.load(
            this,
            "YOUR_AD_UNIT_ID",
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

    private fun showAd() {
        interstitialAd?.show(this)
    }
}

