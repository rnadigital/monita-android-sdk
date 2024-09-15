package com.rnadigital.monita_android

import android.app.Application
import com.rnadigital.monita_android_sdk.SDKInitializer

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//        SDKInitializer.init()
    }
}