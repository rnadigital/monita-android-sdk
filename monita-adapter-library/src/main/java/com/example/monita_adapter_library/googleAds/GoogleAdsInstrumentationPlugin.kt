package com.example.monita_adapter_library.googleAds

import net.bytebuddy.build.Plugin
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.DynamicType
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.asm.Advice
import com.google.android.gms.ads.AdRequest
import net.bytebuddy.dynamic.ClassFileLocator

class GoogleAdsInstrumentationPlugin : Plugin {

    override fun matches(target: TypeDescription): Boolean {
        // Instrument the AdRequest class or other classes from Google Ads SDK
        return target.name == AdRequest::class.java.name
    }

    override fun apply(
        builder: DynamicType.Builder<*>,
        typeDescription: TypeDescription,
        classFileLocator: ClassFileLocator
    ): DynamicType.Builder<*> {
        return builder
            // Intercept loadAd method and delegate to LoadAdAdvice
            .visit(Advice.to(LoadAdAdvice::class.java).on(ElementMatchers.named("loadAd")))
    }

    override fun close() {
        // No resources to close in this example
    }
}
