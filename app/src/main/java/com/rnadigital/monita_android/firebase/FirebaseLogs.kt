package com.rnadigital.monita_android.firebase

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import net.bytebuddy.ByteBuddy
import net.bytebuddy.android.AndroidClassLoadingStrategy
import net.bytebuddy.asm.Advice
import net.bytebuddy.dynamic.scaffold.TypeValidation
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers

class FirebaseLogs(val context: Context) {

    fun setupFirebaseAnalyticsInstrumentation() {
        val classLoader = FirebaseAnalytics::class.java.classLoader

        val strategy = AndroidClassLoadingStrategy.Wrapping(context.getDir("generated", Context.MODE_PRIVATE))

        try {
            // Find the FirebaseAnalytics class and redefine it

            val dynamicType =  ByteBuddy().with(TypeValidation.DISABLED)
                .redefine(FirebaseAnalytics::class.java)
                .method(ElementMatchers.named("logEvent"))
                .intercept(MethodDelegation.to(FirebaseAnalytics::class.java))
                .make()
                .load(classLoader, strategy).loaded

            Log.d("FirebaseInstrumentation", "Firebase Analytics instrumentation applied")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("FirebaseInstrumentation", "Failed to apply instrumentation: ${e.message}")
        }
    }




    class LogEventAdvice {
        @Advice.OnMethodEnter
        fun onEnter(@Advice.Argument(0) eventName: String, @Advice.Argument(1) params: Bundle?) {
            Log.d("FirebaseInterceptor", "logEvent called: eventName = $eventName, params = $params")
        }
    }

    class SetUserPropertyAdvice {
        @Advice.OnMethodEnter
        fun onEnter(@Advice.Argument(0) propertyName: String, @Advice.Argument(1) propertyValue: String?) {
            Log.d("FirebaseInterceptor", "setUserProperty called: propertyName = $propertyName, propertyValue = $propertyValue")
        }
    }






}