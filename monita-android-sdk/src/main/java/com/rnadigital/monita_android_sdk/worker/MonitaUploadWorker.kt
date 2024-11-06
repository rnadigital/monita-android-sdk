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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException

class MonitaUploadWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val apiService = ApiService()
    private val gson = Gson()

    override suspend fun doWork(): Result {
        // Get the JSON string from the input data
        val eventDataJson = inputData.getString("event_data") ?: run {
            Logger().error("Event data is missing.")
            return Result.failure()
        }
        Logger().log("Data successfully sent for eventDataJson ${eventDataJson} .")


        // Deserialize the JSON string into a list of RequestPayload objects
        val requestPayloads: List<RequestPayload> = try {
            val listType = object : TypeToken<List<RequestPayload>>() {}.type
            gson.fromJson(eventDataJson, listType)
        } catch (e: Exception) {
            Logger().error("Error parsing JSON: ${e.message}")
            return Result.failure()
        }

        return try {
            // Refresh the monitoring config using the IO dispatcher
            withContext(Dispatchers.IO) {
                MonitaSDK.refreshMonitoringConfig()
            }

            // Process and send the list of RequestPayloads to the server
            for (payload in requestPayloads) {
                withContext(Dispatchers.IO) {
                    apiService.postData(payload)
                    Logger().log("Data successfully sent for payloads ${payload} .")

                }
            }

            Result.success()
        } catch (e: IOException) {
            Logger().error("Network error: ${e.message}. Retrying...")
            Result.retry() // Retry in case of network-related issues
        } catch (e: Exception) {
            Logger().error("Unexpected error: ${e.message}")
            Result.failure() // Fail if an unexpected error occurs
        }
    }
}
