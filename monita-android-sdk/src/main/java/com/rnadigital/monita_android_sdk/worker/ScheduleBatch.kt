package com.rnadigital.monita_android_sdk.worker
import android.content.Context
import android.os.Bundle
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rnadigital.monita_android_sdk.Logger
import com.rnadigital.monita_android_sdk.MonitaSDK
import com.rnadigital.monita_android_sdk.MonitaSDK.getMaxBatchSize
import com.rnadigital.monita_android_sdk.sendData.ApiService
import com.rnadigital.monita_android_sdk.sendData.RequestPayload


class ScheduleBatch(private val context: Context) {
    private val gson = Gson()
    private val requestBuffer = mutableListOf<RequestPayload>()
    private val maxBatchSize = getMaxBatchSize()

    // Method to add a request and check if it's time to send a batch
    fun addRequestToBatch(
        sdkVersion: String = "1.0",
        appVersion: String = "1.0",
        vendorEvent: String,
        vendorName: String,
        httpMethod: String,
        capturedUrl: String,
        appId: String = "com.rnadigital.monita_android",
        sessionId: String = "",
        consentString: String = "GRANTED",
        hostAppVersion: String = "com.rnadigital.monita_android",
        dtData: List<Map<String, Any>>
    ) {
        // Create the request payload
        val requestPayload = RequestPayload(
            t = MonitaSDK.getSDKToken(),
            dm = "app",
            mv = sdkVersion,
            sv = appVersion,
            tm = System.currentTimeMillis().toDouble() / 1000.0,
            e = vendorEvent,
            vn = vendorName,
            st = "",
            m = httpMethod,
            vu = capturedUrl,
            u = appId,
            p = "",
            dt = dtData,
            rl = sdkVersion,
            `do` = hostAppVersion,
            cn = consentString,
            sid = sessionId,
            cid = ""
        )

        // Add the request to the buffer
        requestBuffer.add(requestPayload)
        Logger().log("MonitaUploadWorker", "requestBuffer.size ${requestBuffer.size}")
        Logger().log("MonitaUploadWorker", "maxBatchSize ${maxBatchSize}")


        // If the buffer has reached the maximum batch size, send the batch
        if (requestBuffer.size >= maxBatchSize) {
            sendBatch()

        }
    }

    // Method to send the batch using WorkManager
    private fun sendBatch() {
        // Convert the list of RequestPayload to JSON
        val requestDataJson = gson.toJson(requestBuffer)

        // Create input data for WorkManager
        val data = Data.Builder()
            .putString("event_data", requestDataJson)
            .build()

        // Create and enqueue the WorkRequest
        val uploadWorkRequest = OneTimeWorkRequestBuilder<MonitaUploadWorker>()
            .setInputData(data)
            .build()
        Logger().log("MonitaUploadWorker", "sending batch to server")


        WorkManager.getInstance(context).enqueue(uploadWorkRequest)

        // Clear the buffer after enqueuing the work
        requestBuffer.clear()
        Logger().log("MonitaUploadWorker", "requestBuffer.clear")

    }
}

// Singleton instance of ScheduleBatch
object ScheduleBatchManager {
    lateinit var scheduleBatch: ScheduleBatch

    fun initialize(context: Context) {
        scheduleBatch = ScheduleBatch(context)
    }
}

