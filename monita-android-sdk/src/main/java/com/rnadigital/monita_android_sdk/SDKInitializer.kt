package com.rnadigital.monita_android_sdk

import okhttp3.OkHttpClient

object SDKInitializer {
    private var isInitialized = false

    fun init() {
        if (isInitialized) return

        // Set up OkHttpClient with the interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(NetworkInterceptor(Logger()))
            .build()

        // Save client for later use
        OkHttpClientProvider.setClient(client)

        isInitialized = true
    }
}