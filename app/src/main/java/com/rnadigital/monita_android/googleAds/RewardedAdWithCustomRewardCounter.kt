package com.rnadigital.monita_android.googleAds

import androidx.compose.runtime.Composable
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
@Composable
fun RewardedAdWithCustomRewardCounter() {
    val context = LocalContext.current
    var rewardedAd by remember { mutableStateOf<RewardedAd?>(null) }
    var rewardCount by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = true) {
        loadRewardedAd(context) { ad ->
            rewardedAd = ad
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                rewardedAd?.show(context as Activity) { rewardItem ->
                    rewardCount += 1
                    Log.d("RewardedAd", "User rewarded with ${rewardItem.amount} ${rewardItem.type}")
                }
            },
            enabled = rewardedAd != null
        ) {
            Text("Show Rewarded Ad")
        }

        if (rewardCount > 0) {
            Text(
                text = "You have been rewarded $rewardCount times!",
                style = MaterialTheme.typography.h6
            )
        }
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
