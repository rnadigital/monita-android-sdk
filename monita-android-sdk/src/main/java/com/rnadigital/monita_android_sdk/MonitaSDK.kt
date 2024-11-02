package com.rnadigital.monita_android_sdk

import android.content.Context
import com.google.gson.Gson
import com.rnadigital.monita_android_sdk.monitoringConfig.MonitoringConfig
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import java.lang.ref.WeakReference
import android.provider.Settings

object MonitaSDK {
    private var isInitialized = false
    private var enableLogger = false
    private val logger = Logger()
    private lateinit var monitoringConfig: MonitoringConfig
    private var token: String = ""
    private var contextReference: WeakReference<Context>? = null




    class Builder(private val context: Context) {

        fun enableLogger(loggerEnabled: Boolean): Builder = apply { enableLogger = loggerEnabled }
        fun setToken(t: String): Builder = apply { token = t }

        fun build(onInitialized: (() -> Unit)? = null) {
            init(context, onInitialized)
        }
    }

    //get CID
    //get SID
    // Get CN
    // Refresh fetchMonitoringConfig
    // enable Logs

   private fun init(
        context: Context,
        onInitialized: (() -> Unit)? = null
    ) {
        if (isInitialized) {
            onInitialized?.invoke() // Already initialized, notify immediately
            return
        }


       contextReference = WeakReference(context.applicationContext)

        fetchMonitoringConfig(token) { monitoringConfig ->
            this.monitoringConfig = monitoringConfig
            isInitialized = true
            onInitialized?.invoke()
            logger.log("Fetching monitoring Config")
        }
    }

    fun refreshMonitoringConfig() {
        fetchMonitoringConfig(token) { monitoringConfig ->
            this.monitoringConfig = monitoringConfig
            isInitialized = true
            logger.log("Refreshing monitoring Config")
        }
    }

    fun isLoggerEnabled(): Boolean {
        return enableLogger
    }

    fun getMonitaContext(): Context? {
        return contextReference?.get() // Returns the application context if available
    }

    fun getMonitoringConfig(): MonitoringConfig {
        return monitoringConfig // Returns the application context if available
    }

    fun getSDKToken(): String {
        return token // Returns the application context if available
    }

    fun isSDKInitialized(): Boolean {
        return isInitialized
    }


    // Function to get a unique session ID
    fun getSessionId(context: Context): String {
        // Example of generating a unique session ID using Android's Settings.Secure class
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            ?: "default_session_id"
    }

    // Function to get a unique customer ID
    fun getCustomerId(): String {
        // Custom code to generate or fetch a customer ID
        // For this example, we'll use a hardcoded string or return a generated UUID
        return "customer_id_12345" // Replace this with your logic to retrieve or generate a customer ID
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
                    logger.log("monitoringConfig ${monitoringConfig.toString()}")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // Handle failure (retry, fallback, etc.)
            }
        })
    }
}