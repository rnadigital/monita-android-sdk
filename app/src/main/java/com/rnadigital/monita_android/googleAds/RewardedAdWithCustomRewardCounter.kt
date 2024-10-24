package com.rnadigital.monita_android.googleAds

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
