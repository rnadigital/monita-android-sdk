package com.example.monita_adapter_library.googleAds

import android.os.Bundle
import com.example.SendDataToServer
import com.facebook.FacebookSdk
import net.bytebuddy.asm.Advice
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.mediation.customevent.CustomEvent

object LoadAdAdvice {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(
        @Advice.This adRequest: AdRequest,
        @Advice.Argument(0) adUnitId: String
    ) {
        // Create a Bundle to store ad request details
        val adDataBundle = Bundle().apply {
            // Basic ad info
            putString("ad_unit_id", adUnitId)
            putBoolean("is_test_device", adRequest.isTestDevice(FacebookSdk.getApplicationContext()))
            putString("content_url", adRequest.getContentUrl())
            putString("request_agent", adRequest.getRequestAgent())
            putString("ad_string", adRequest.getAdString())
            putString("device_id_emulator", AdRequest.DEVICE_ID_EMULATOR)

            // Keywords
            val keywords = adRequest.getKeywords().joinToString(", ")
            putString("keywords", keywords)

            // Error codes (for potential troubleshooting/logging)
            putInt("error_code_internal_error", AdRequest.ERROR_CODE_INTERNAL_ERROR)
            putInt("error_code_invalid_request", AdRequest.ERROR_CODE_INVALID_REQUEST)
            putInt("error_code_network_error", AdRequest.ERROR_CODE_NETWORK_ERROR)
            putInt("error_code_no_fill", AdRequest.ERROR_CODE_NO_FILL)
            putInt("error_code_app_id_missing", AdRequest.ERROR_CODE_APP_ID_MISSING)
            putInt("error_code_request_id_mismatch", AdRequest.ERROR_CODE_REQUEST_ID_MISMATCH)
            putInt("error_code_invalid_ad_string", AdRequest.ERROR_CODE_INVALID_AD_STRING)
            putInt("error_code_mediation_no_fill", AdRequest.ERROR_CODE_MEDIATION_NO_FILL)

            // Content URL properties
            putInt("max_content_url_length", AdRequest.MAX_CONTENT_URL_LENGTH)
            val neighboringContentUrls = adRequest.getNeighboringContentUrls().joinToString(", ")
            putString("neighboring_content_urls", neighboringContentUrls)

            // Custom targeting and network extras (if available)
            val customTargetingBundle = adRequest.getCustomTargeting()
            putBundle("custom_targeting", customTargetingBundle)

            val customEventExtras = adRequest.getCustomEventExtrasBundle(GenericCustomEvent::class.java)
            customEventExtras?.let {
                putBundle("custom_event_extras", it)
            }
        }

        // Log ad details for debugging purposes
        println("Intercepted Google Ad loadAd: adUnitId=$adUnitId")
        println("AdRequest parameters: $adDataBundle")

        // Send data to server using SendDataToServer class
        SendDataToServer().uploadGoogleAdsData("load_ad_event", adDataBundle)
    }

    @JvmStatic
    @Advice.OnMethodExit
    fun onExit() {
        // This code runs after the original loadAd method is called
        println("Google Ad loadAd method has been executed.")
    }
}

// Placeholder class implementing CustomEvent for accessing custom event extras
class GenericCustomEvent : CustomEvent {
    override fun onDestroy() {}
    override fun onPause() {}
    override fun onResume() {}
}
