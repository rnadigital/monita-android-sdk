package com.rnadigital.monita_android_sdk.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rnadigital.monita_android_sdk.Logger
import com.rnadigital.monita_android_sdk.MonitaSDK
import com.rnadigital.monita_android_sdk.sendData.ApiService
import com.rnadigital.monita_android_sdk.sendData.RequestPayload
import kotlinx.coroutines.coroutineScope

class MonitaUploadWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val apiService = ApiService()
    private val gson = Gson()

    override suspend fun doWork(): Result = coroutineScope {
        // Get the JSON string from the input data
        val eventDataJson = inputData.getString("event_data") ?: return@coroutineScope Result.failure()

        // Deserialize the JSON string into a list of RequestPayload objects
        val listType = object : TypeToken<List<RequestPayload>>() {}.type
        val requestPayloads: List<RequestPayload> = try {
            gson.fromJson(eventDataJson, listType)
        } catch (e: Exception) {
            Logger().error("Error parsing JSON: ${e.message}")
            return@coroutineScope Result.failure()
        }

        try {
            // Refresh the monitoring config before sending data
            MonitaSDK.refreshMonitoringConfig()

            // Process the list of RequestPayloads and send them to the server
            for (payload in requestPayloads) {
                apiService.postData(payload)
            }
            Result.success()
        } catch (e: Exception) {
            Logger().error("Error sending data: ${e.message}")
            Result.retry()
        }
    }
}
