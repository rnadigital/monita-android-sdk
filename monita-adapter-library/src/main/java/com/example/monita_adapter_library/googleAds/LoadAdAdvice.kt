package com.example.monita_adapter_library.googleAds

import net.bytebuddy.asm.Advice
import com.google.android.gms.ads.AdRequest

object LoadAdAdvice {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(
        @Advice.This adRequest: AdRequest,
        @Advice.Argument(0) adUnitId: String
    ) {
        // Log the AdRequest details before the loadAd method is executed
        println("Intercepted Google Ad loadAd: adUnitId=$adUnitId")
        println("AdRequest parameters: ${adRequest.toString()}")

        // Example: Modify the adUnitId or log request details
        // Note: Direct modifications may require more complex handling
    }

    @JvmStatic
    @Advice.OnMethodExit
    fun onExit() {
        // This code runs after the original loadAd method is called
        println("Google Ad loadAd method has been executed.")
    }
}
