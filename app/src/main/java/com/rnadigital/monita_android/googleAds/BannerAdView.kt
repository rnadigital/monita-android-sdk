package com.rnadigital.monita_android.googleAds

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

@Composable
fun BannerAdView(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val adView = remember {
        AdView(context).apply {
//            adSize = AdSize.BANNER
            adUnitId = "YOUR_BANNER_AD_UNIT_ID"
            loadAd(AdRequest.Builder().build())
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Log.d("BannerAd", "Ad loaded successfully")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("BannerAd", "Failed to load ad: ${error.message}")
                }

                override fun onAdClicked() {
                    Log.d("BannerAd", "Ad clicked")
                }
            }
        }
    }

    AndroidView(
        factory = { adView },
        modifier = modifier.fillMaxWidth()
    )
}
