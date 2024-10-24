package com.rnadigital.monita_android.googleAds
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.rnadigital.monita_android.R

@Composable
fun NativeAdViewExample() {
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    LaunchedEffect(key1 = true) {
        loadNativeAd(context) { ad ->
            nativeAd = ad
        }
    }

    nativeAd?.let { ad ->
        AndroidView(
            factory = { context ->
                val adView = LayoutInflater.from(context).inflate(R.layout.native_ad_layout, null) as NativeAdView
                populateNativeAdView(ad, adView)
                adView
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun loadNativeAd(context: Context, onAdLoaded: (NativeAd) -> Unit) {
    val adLoader = AdLoader.Builder(context, "YOUR_NATIVE_AD_UNIT_ID")
        .forNativeAd { ad: NativeAd ->
            onAdLoaded(ad)
        }
        .withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(error: LoadAdError) {
                Log.e("NativeAd", "Failed to load ad: ${error.message}")
            }
        })
        .build()
    adLoader.loadAd(AdRequest.Builder().build())
}

private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
    adView.findViewById<TextView>(R.id.ad_headline)?.text = nativeAd.headline
    adView.findViewById<MediaView>(R.id.ad_media)?.setMediaContent(nativeAd.mediaContent)
    adView.setNativeAd(nativeAd)
}
