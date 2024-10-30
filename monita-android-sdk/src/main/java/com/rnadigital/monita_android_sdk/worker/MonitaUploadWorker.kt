package com.rnadigital.monita_android_sdk.worker

import com.rnadigital.monita_android_sdk.sendData.ApiService

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rnadigital.monita_android_sdk.MonitaSDK
import com.rnadigital.monita_android_sdk.sendData.RequestPayload
import okhttp3.OkHttpClient
import java.io.IOException

class MonitaUploadWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val apiService = ApiService()
    private val gson = Gson()

    override fun doWork(): Result {
        // Retrieve JSON string from input data
        val eventDataJson = inputData.getString("event_data") ?: return Result.failure()

        // Deserialize JSON string to RequestPayload object
        val requestPayload: RequestPayload = gson.fromJson(eventDataJson, RequestPayload::class.java)

        MonitaSDK.refreshMonitoringConfig()


        return try {
            // Send data using ApiService
            apiService.postData( requestPayload)
            Log.d("MonitaUploadWorker", "Successfully posted batch data")
            Result.success()
        } catch (e: IOException) {
            Log.e("MonitaUploadWorker", "Failed to post data", e)
            Result.retry() // Retry the work in case of failure
        }
    }
}
