package com.example.monita_adapter_library.googleAds

import net.bytebuddy.build.Plugin
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.DynamicType
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.asm.Advice
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import net.bytebuddy.dynamic.ClassFileLocator

class GoogleAdsInstrumentationPlugin : Plugin {

    override fun matches(target: TypeDescription): Boolean {
        return target.name == AdManagerInterstitialAd::class.java.name
    }

    override fun apply(
        builder: DynamicType.Builder<*>,
        typeDescription: TypeDescription,
        classFileLocator: ClassFileLocator
    ): DynamicType.Builder<*> {
        return builder
            // Intercept the load method
            .visit(Advice.to(LoadAdAdvice::class.java).on(ElementMatchers.named("load")))
    }

    override fun close() {
        // No resources to close in this example
    }

}
