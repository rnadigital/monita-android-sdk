package com.example.monita_adapter_library.googleAds

import android.content.Context
import android.os.Bundle
import com.example.SendDataToServer
import net.bytebuddy.asm.Advice
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback

object LoadAdAdvice {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(
        @Advice.Argument(0) context: Context,
        @Advice.Argument(1) adUnitId: String,
        @Advice.Argument(2) adRequest: AdManagerAdRequest,
        @Advice.Argument(3) callback: AdManagerInterstitialAdLoadCallback
    ) {
        // Log details of the ad request
        val adDataBundle = Bundle().apply {
            putString("ad_unit_id", adUnitId)
            putString("content_url", adRequest.contentUrl)
            putBoolean("is_test_device", adRequest.isTestDevice(context))
            putBundle("custom_targeting", adRequest.customTargeting)
        }

        // Log for debugging purposes
        println("Intercepted AdManagerInterstitialAd.load: adUnitId=$adUnitId, adRequest=$adDataBundle")

        // Send data to server
        SendDataToServer().uploadGoogleAdsData("load_ad_event", adDataBundle)
    }

    @JvmStatic
    @Advice.OnMethodExit
    fun onExit() {
        println("AdManagerInterstitialAd.load has been called.")
    }
}
