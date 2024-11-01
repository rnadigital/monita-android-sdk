package com.rnadigital.monita_android_sdk.worker
import android.content.Context
import android.os.Bundle
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rnadigital.monita_android_sdk.MonitaSDK
import com.rnadigital.monita_android_sdk.sendData.ApiService
import com.rnadigital.monita_android_sdk.sendData.RequestPayload


class ScheduleBatch(private val context: Context) {
    private val gson = Gson()



    // Method to schedule batch upload
    fun scheduleBatchUpload(
        sdkVersion: String = "1.0",          // SDK version
        appVersion: String = "1.0",          // App version
        vendorEvent: String,         // Vendor event
        vendorName: String,          // Vendor name (case-sensitive)
        httpMethod: String,          // HTTP method (POST)
        capturedUrl: String,         // Captured HTTP endpoint URL
        appId: String = "com.rnadigital.monita_android",               // App ID
        sessionId: String ="",           // Session ID
        consentString: String = "GRANTED",       // Consent string value
        hostAppVersion: String = "com.rnadigital.monita_android",      // Host app version
        dtData: List<Map<String, Any>>         // Dynamic payload content
    ) {
        val requestPayload :RequestPayload = RequestPayload(
            t = MonitaSDK.getSDKToken(),
            dm = "app", // Deployment method (app for SDK based deployments)
            mv = sdkVersion,
            sv = appVersion,
            tm = System.currentTimeMillis().toDouble() / 1000.0, // Unix time in seconds with milliseconds
            e = vendorEvent,
            vn = vendorName,
            st = "", // HTTP call status, can be success/failed (for now it's null)
            m = httpMethod,
            vu = capturedUrl,
            u = appId,
            p = "",  // App area or null
            dt = dtData, // Dynamic payload data
            rl = sdkVersion, // SDK release version
            `do` = hostAppVersion,
            cn = consentString,
            sid = sessionId,
            cid = "" // Customer ID (null or SDK generated)
        )

        val data = Data.Builder()
            .putString("event_data", gson.toJson(requestPayload))
            .build()

        val uploadWorkRequest = OneTimeWorkRequestBuilder<MonitaUploadWorker>()
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(uploadWorkRequest)
    }


    private fun bundleToMap(bundle: Bundle): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        for (key in bundle.keySet()) {
            map[key] = bundle[key] ?: ""
        }
        return map
    }
}
