package com.rnadigital.monita_android.googleAds
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.delay

@Composable
fun BackPressInterstitialAd() {
    val context = LocalContext.current
    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
    var isAdLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        loadInterstitialAd(context) { ad ->
            interstitialAd = ad
            isAdLoaded = true
        }
    }

    BackHandler(enabled = isAdLoaded) {
        interstitialAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("InterstitialAd", "Ad dismissed")
                    isAdLoaded = false
                }

                override fun onAdShowedFullScreenContent() {
                    interstitialAd = null
                }
            }
            ad.show(context as Activity)
        }
    }

    if (!isAdLoaded) {
        Text("Loading interstitial ad...")
    }
}

private fun loadInterstitialAd(context: Context, onAdLoaded: (InterstitialAd) -> Unit) {
    InterstitialAd.load(
        context,
        "YOUR_INTERSTITIAL_AD_UNIT_ID",
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                onAdLoaded(ad)
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                Log.e("InterstitialAd", "Failed to load ad: ${error.message}")
            }
        }
    )
}
