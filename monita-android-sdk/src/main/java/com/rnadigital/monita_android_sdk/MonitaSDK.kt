package com.rnadigital.monita_android_sdk

import com.google.gson.Gson
import com.rnadigital.monita_android_sdk.monitoringConfig.MonitoringConfig
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException

object MonitaSDK {
    var isInitialized = false
    lateinit var monitoringConfig: MonitoringConfig
    var token = "fe041147-0600-48ad-a04e-d3265becc4eb"

    fun init(token: String, onInitialized: (() -> Unit)? = null) {
        if (isInitialized) {
            onInitialized?.invoke() // Already initialized, notify immediately
            this.token = token
            return
        }

        fetchMonitoringConfig(token) { monitoringConfig ->
            this.monitoringConfig = monitoringConfig
            isInitialized = true
            onInitialized?.invoke()
        }
    }



    private fun fetchMonitoringConfig(token: String, callback: (MonitoringConfig) -> Unit) {

        val unixTime = System.currentTimeMillis()


        val request = Request.Builder()
            .url("https://storage.googleapis.com/cdn-monita-dev/custom-config/$token.json?v=$unixTime")
            .build()

        val client = OkHttpClient.Builder().build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val jsonResponse = response.body?.string()
                if (jsonResponse != null) {
                    // Parse the JSON response into MonitoringConfig
                    val gson = Gson()
                    val monitoringConfig = gson.fromJson(jsonResponse, MonitoringConfig::class.java)
                    callback(monitoringConfig)
                    println("monitoringConfig ${monitoringConfig.toString()}")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // Handle failure (retry, fallback, etc.)
            }
        })
    }
}