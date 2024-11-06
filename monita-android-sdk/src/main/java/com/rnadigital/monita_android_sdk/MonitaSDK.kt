package com.rnadigital.monita_android_sdk

import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import com.google.gson.Gson
import com.rnadigital.monita_android_sdk.monitoringConfig.MonitoringConfig
import com.rnadigital.monita_android_sdk.worker.ScheduleBatchManager
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


object MonitaSDK {
    private var isInitialized = false
    private var enableLogger = false
    private val logger = Logger()
    private lateinit var monitoringConfig: MonitoringConfig
    private var token: String = ""
    private var contextReference: WeakReference<Context>? = null
    private var maxBatchSize = 2
    private var customerId: String = ""
    private var sessionId: String = ""
    private var sdkVersion = ""
    private var appVersion = ""
    private var appId = ""
    const val SDK_VERSION = "1.0.0"



    // Thread pool for background tasks
    private val executorService = Executors.newSingleThreadExecutor()
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Builder class for SDK configuration
    class Builder(private val context: Context) {
        fun enableLogger(loggerEnabled: Boolean): Builder = apply { enableLogger = loggerEnabled }
        fun setToken(t: String): Builder = apply { token = t }
        fun setBatchSize(maxSize: Int = 2): Builder = apply { maxBatchSize = maxSize }
        fun setCID(cid: String = ""): Builder = apply { customerId = cid }
        fun setAppVersion(version: String = ""): Builder = apply { appVersion = version }
        fun build(onInitialized: (() -> Unit)? = null) {
            init(context, onInitialized)
        }
    }

    // Initialization method
    private fun init(context: Context, onInitialized: (() -> Unit)? = null) {
        if (isInitialized) {
            onInitialized?.invoke() // Already initialized, notify immediately
            return
        }
        contextReference = WeakReference(context.applicationContext)
        ScheduleBatchManager.initialize(context)

        sessionId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "default_session_id"
        appId = context.packageName
        // Fetch monitoring config on a background thread
        executorService.execute {
            try {
                fetchMonitoringConfig(token) { config ->
                    monitoringConfig = config
                    isInitialized = true
                    logger.log("Monitoring Config fetched successfully")
                    onInitialized?.invoke()
                }
            } catch (e: Exception) {
                logger.error("Initialization failed: ${e.message}")
            }
        }
    }


    // Function to refresh the monitoring config and wait for the call to complete
    suspend fun refreshMonitoringConfig() = withContext(Dispatchers.IO) {
        try {
            // Use a CompletableDeferred to wait for the callback to complete
            val deferred = CompletableDeferred<Unit>()

            fetchMonitoringConfig(token) { config ->
                monitoringConfig = config
                logger.log("Monitoring Config refreshed successfully")
                deferred.complete(Unit) // Complete the deferred when done
            }

            deferred.await() // Wait for the deferred to complete
        } catch (e: Exception) {
            logger.error("Failed to refresh Monitoring Config: ${e.message}")
        }
    }

    // Public getters
    fun isLoggerEnabled(): Boolean = enableLogger
    fun getMonitaContext(): Context? = contextReference?.get()
    fun getMonitoringConfig(): MonitoringConfig = monitoringConfig
    fun getSDKToken(): String = token
    fun isSDKInitialized(): Boolean = isInitialized
    fun getMaxBatchSize(): Int = maxBatchSize
    fun getSdkVersion(): String = sdkVersion
    fun getAppVersion(): String = appVersion
    fun getAppId(): String = appId

    // Unique session ID
    fun getSessionId(): String = sessionId


    // Example customer ID
    fun getCustomerId(): String = customerId


  private  fun sdkVersion(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "Unknown"
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown"
        }
    }


    // Improved fetchMonitoringConfig method with error handling
    private fun fetchMonitoringConfig(token: String, callback: (MonitoringConfig) -> Unit) {
        val unixTime = System.currentTimeMillis()
        val request = Request.Builder()
            .url("https://storage.googleapis.com/cdn-monita-dev/custom-config/$token.json?v=$unixTime")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonResponse = response.body?.string()
                    if (jsonResponse != null) {
                        val gson = Gson()
                        val config = gson.fromJson(jsonResponse, MonitoringConfig::class.java)
                        callback(config)
                        logger.log("Monitoring Config: $config")
                    } else {
                        logger.error("Empty response body")
                    }
                } else {
                    logger.error("Error fetching config: ${response.code} ${response.message}")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                logger.error("Network request failed: ${e.message}")
            }
        })
    }
}
