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
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

@Composable
fun RewardedAdExample() {
    val context = LocalContext.current
    var rewardedAd by remember { mutableStateOf<RewardedAd?>(null) }
    var userRewarded by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        loadRewardedAd(context) { ad ->
            rewardedAd = ad
        }
    }

    Button(
        onClick = {
            rewardedAd?.show(context as Activity) { rewardItem ->
                userRewarded = true
                Log.d("RewardedAd", "User rewarded with ${rewardItem.amount} ${rewardItem.type}")
            }
        },
        enabled = rewardedAd != null
    ) {
        Text("Show Rewarded Ad")
    }

    if (userRewarded) {
        Text("User received reward!")
    }
}

private fun loadRewardedAd(context: Context, onAdLoaded: (RewardedAd) -> Unit) {
    RewardedAd.load(
        context,
        "YOUR_REWARDED_AD_UNIT_ID",
        AdRequest.Builder().build(),
        object : RewardedAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedAd) {
                onAdLoaded(ad)
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                Log.e("RewardedAd", "Failed to load ad: ${error.message}")
            }
        }
    )
}
