package com.rnadigital.monita_android_sdk.OkHttp

import com.rnadigital.monita_android_sdk.OkHttpClientProvider
import okhttp3.OkHttpClient

// Custom Builder Wrapper for OkHttpClient
class OkHttpClientBuilderWrapper(private val originalBuilder: OkHttpClient.Builder) {

    fun addInterceptor(interceptor: okhttp3.Interceptor): OkHttpClientBuilderWrapper {
        originalBuilder.addInterceptor(interceptor)
        return this
    }

    fun connectTimeout(timeout: Long, unit: java.util.concurrent.TimeUnit): OkHttpClientBuilderWrapper {
        originalBuilder.connectTimeout(timeout, unit)
        return this
    }

    // Add other methods from OkHttpClient.Builder that you might want to customize

    // Build method to return the client from OkHttpClientProvider
    fun build(): OkHttpClient {
        return OkHttpClientProvider.getClient()
    }
}


