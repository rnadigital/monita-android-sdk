package com.example.monita_adapter_library.firebase_analytics


import net.bytebuddy.build.Plugin
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.DynamicType
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.asm.Advice
import com.google.firebase.analytics.FirebaseAnalytics
import net.bytebuddy.dynamic.ClassFileLocator

class FirebaseAnalyticsInstrumentationPlugin : Plugin {

    override fun matches(target: TypeDescription): Boolean {
        // Instrument the FirebaseAnalytics class
        return target.name == FirebaseAnalytics::class.java.name
    }

    override fun apply(
        builder: DynamicType.Builder<*>,
        typeDescription: TypeDescription,
        classFileLocator: ClassFileLocator
    ): DynamicType.Builder<*> {
        return builder
            // Intercept logEvent method
            .visit(Advice.to(LogEventAdvice::class.java).on(ElementMatchers.named("logEvent")))
            // Intercept setDefaultEventParameters method
            .visit(Advice.to(SetDefaultEventParametersAdvice::class.java).on(ElementMatchers.named("setDefaultEventParameters")))
            // Intercept setCurrentScreen method
            .visit(Advice.to(SetCurrentScreenAdvice::class.java).on(ElementMatchers.named("setCurrentScreen")))
            // Intercept setUserId method
            .visit(Advice.to(SetUserIdAdvice::class.java).on(ElementMatchers.named("setUserId")))
            // Intercept setUserProperty method
            .visit(Advice.to(SetUserPropertyAdvice::class.java).on(ElementMatchers.named("setUserProperty")))
            // Intercept setAnalyticsCollectionEnabled method
            .visit(Advice.to(SetAnalyticsCollectionEnabledAdvice::class.java).on(ElementMatchers.named("setAnalyticsCollectionEnabled")))
    }

    override fun close() {
        // No resources to close in this example
    }
}
