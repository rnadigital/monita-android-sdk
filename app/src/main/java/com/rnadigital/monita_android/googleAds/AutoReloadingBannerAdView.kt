package com.rnadigital.monita_android.googleAds
import android.app.Activity
import android.content.Context
import android.util.Log
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
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.delay

@Composable
fun AutoReloadingBannerAdView(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var adView by remember { mutableStateOf<AdView?>(null) }
    var isAdLoaded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = isAdLoaded) {
        if (!isAdLoaded) {
            delay(30000) // Reload ad every 30 seconds if not loaded
            adView?.loadAd(AdRequest.Builder().build())
        }
    }

    adView = remember {
        AdView(context).apply {
            adSize = AdSize.BANNER
            adUnitId = "YOUR_BANNER_AD_UNIT_ID"
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    isAdLoaded = true
                    Log.d("BannerAd", "Ad loaded successfully")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    isAdLoaded = false
                    Log.e("BannerAd", "Failed to load ad: ${error.message}")
                }

                override fun onAdClicked() {
                    isAdLoaded = false // Reset to force reload after click
                    Log.d("BannerAd", "Ad clicked")
                }
            }
        }
    }

    AndroidView(
        factory = { adView!! },
        modifier = modifier.fillMaxWidth()
    )
}
