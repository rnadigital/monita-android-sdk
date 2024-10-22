package com.rnadigital.monita_android

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.rnadigital.monita_android.firebase.FirebaseLogs
import com.rnadigital.monita_android_sdk.Logger
import com.rnadigital.monita_android_sdk.MonitaSDK
import net.bytebuddy.ByteBuddy
import net.bytebuddy.android.AndroidClassLoadingStrategy
import net.bytebuddy.asm.Advice
import net.bytebuddy.dynamic.ClassFileLocator
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy
import net.bytebuddy.dynamic.scaffold.TypeValidation
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.pool.TypePool
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException


class MyApplication : Application() {
    val token = "fe041147-0600-48ad-a04e-d3265becc4eb"


    override fun onCreate() {
        super.onCreate()

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        MonitaSDK.init (token){}

        FacebookSdk.sdkInitialize(this)
        AppEventsLogger.activateApp(this)




    }
}


