package com.rnadigital.monita_android_sdk

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.rnadigital.monita_android_sdk.monitoringConfig.MonitoringConfig
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException

object SDKInitializer {
    private var isInitialized = false


    fun init( onInitialized: (() -> Unit)? = null) {
        if (isInitialized) {
            onInitialized?.invoke() // Already initialized, notify immediately
            return
        }

        // Fetch the monitoring configuration from the API
        fetchMonitoringConfig { monitoringConfig ->
            // Set up OkHttpClient with the interceptor and monitoring config
            val client = OkHttpClient.Builder()
                .addInterceptor(NetworkInterceptor(Logger(), monitoringConfig))
                .build()

            // Save client for later use
            OkHttpClientProvider.setClient(client)

            isInitialized = true

            // Notify that initialization is complete
            onInitialized?.invoke()
        }
    }

//    fun init() {
//        try {
//            if (isInitialized) return
//
//            fetchMonitoringConfig { monitoringConfig ->
//                // Set up OkHttpClient with the interceptor and monitoring config
//                val client = OkHttpClient.Builder()
//                    .addInterceptor(NetworkInterceptor(Logger(), monitoringConfig))
//                    .build()
//
//
//                Log.d("SDKLogger", "init: SDKInitialized")
//                // Save client for later use
//                OkHttpClientProvider.setClient(client)
//
//                isInitialized = true
//            }
//        } catch (e: Exception) {
//            Log.e("SDKInitializer", "Failed to initialize SDK", e)
//        }
//    }


    private fun fetchMonitoringConfig(callback: (MonitoringConfig) -> Unit) {
        val request = Request.Builder()
            .url("https://storage.googleapis.com/cdn-monita-dev/custom-config/fe041147-0600-48ad-a04e-d3265becc4eb.json")
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val jsonResponse = response.body?.string()
                if (jsonResponse != null) {
                    // Parse the JSON response into MonitoringConfig
                    val gson = Gson()
                    val monitoringConfig = gson.fromJson(jsonResponse, MonitoringConfig::class.java)
                    callback(monitoringConfig)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // Handle failure (retry, fallback, etc.)
            }
        })
    }
}