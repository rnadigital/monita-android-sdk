package com.example.monita_adapter_library.adobeAnalytics

import com.adobe.marketing.mobile.Analytics
import com.adobe.marketing.mobile.MobileCore
import net.bytebuddy.build.Plugin
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.DynamicType
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.asm.Advice
import net.bytebuddy.dynamic.ClassFileLocator

class AdobeAnalyticsInstrumentationPlugin : Plugin {

    override fun matches(target: TypeDescription): Boolean {
        return target.name == MobileCore::class.java.name
    }

    override fun apply(
        builder: DynamicType.Builder<*>,
        typeDescription: TypeDescription,
        classFileLocator: ClassFileLocator
    ): DynamicType.Builder<*> {
        return builder
            // Intercept trackState method
            .visit(Advice.to(TrackStateAdvice::class.java).on(ElementMatchers.named("trackState")))
            // Intercept trackAction method
            .visit(Advice.to(TrackActionAdvice::class.java).on(ElementMatchers.named("trackAction")))
    }

    override fun close() {
        // No resources to close in this example
    }
}
