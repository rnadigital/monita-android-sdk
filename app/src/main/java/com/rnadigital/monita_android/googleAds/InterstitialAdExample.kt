package com.rnadigital.monita_android.googleAds

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

@Composable
fun InterstitialAdExample() {
    val context = LocalContext.current
    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
    var isAdLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        loadInterstitialAd(context, isAdLoading) { ad ->
            interstitialAd = ad
        }
    }

    Button(
        onClick = {
            interstitialAd?.show(context as Activity)
        },
        enabled = interstitialAd != null
    ) {
        Text("Show Interstitial Ad")
    }
}



private fun loadInterstitialAd(context: Context, isAdLoading: Boolean, onAdLoaded: (InterstitialAd) -> Unit) {
    if (!isAdLoading) {
//        isAdLoading = true
        InterstitialAd.load(
            context,
            "YOUR_INTERSTITIAL_AD_UNIT_ID",
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
//                    isAdLoading = false
                    onAdLoaded(ad)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
//                    isAdLoading = false
                    Log.e("InterstitialAd", "Failed to load ad: ${error.message}")
                }
            }
        )
    }
}
