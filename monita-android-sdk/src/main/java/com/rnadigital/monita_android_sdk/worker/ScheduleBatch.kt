package com.rnadigital.monita_android_sdk.worker
import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.gson.Gson
import com.rnadigital.monita_android_sdk.Logger
import com.rnadigital.monita_android_sdk.MonitaSDK
import com.rnadigital.monita_android_sdk.MonitaSDK.getMaxBatchSize
import com.rnadigital.monita_android_sdk.monitaDatabase.RequestPayloadEntity
import com.rnadigital.monita_android_sdk.monitaDatabase.SDKDatabase
import com.rnadigital.monita_android_sdk.sendData.RequestPayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ScheduleBatch(private val context: Context) {
    private val gson = Gson()
    private val requestBuffer = mutableListOf<RequestPayload>()
    private val maxBatchSize = getMaxBatchSize()
    private val database = SDKDatabase.getDatabase(context)
    private val dao = database.requestPayloadDao()

    // Method to add a request and check if it's time to send a batch
    fun addRequestToBatch(
        sdkVersion: String = MonitaSDK.getSdkVersion(),
        appVersion: String = MonitaSDK.getAppVersion(),
        vendorEvent: String,
        vendorName: String,
        httpMethod: String,
        capturedUrl: String,
        appId: String = MonitaSDK.getAppId(),
        sessionId: String = MonitaSDK.getSessionId(),
        consentString: String = "GRANTED",
        hostAppVersion: String = "com.rnadigital.monita_android",
        dtData: List<Map<String, Any>>
    ) {
//        // Create the request payload
//        val requestPayload = RequestPayload(
//            t = MonitaSDK.getSDKToken(),
//            dm = "app",
//            mv = sdkVersion,
//            sv = appVersion,
//            tm = System.currentTimeMillis().toDouble() / 1000.0,
//            e = vendorEvent,
//            vn = vendorName,
//            st = "",
//            m = httpMethod,
//            vu = capturedUrl,
//            u = appId,
//            p = "",
//            dt = dtData,
//            rl = sdkVersion,
//            `do` = hostAppVersion,
//            cn = consentString,
//            sid = sessionId,
//            cid = MonitaSDK.getCustomerId()
//        )



        // Convert dtData to JSON string
        val dtDataJson = gson.toJson(dtData)

        // Create the RequestPayloadEntity object
        val requestPayload = RequestPayloadEntity(
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
            domain = hostAppVersion,
            cn = consentString,
            sid = sessionId,
            cid = MonitaSDK.getCustomerId()
        )


        // Add the request to the Room database
        CoroutineScope(Dispatchers.IO).launch {
            dao.insert(requestPayload)
            checkAndSendBatch()
        }

//        // Add the request to the buffer
//        requestBuffer.add(requestPayload)
//        Logger().log("MonitaUploadWorker", "requestBuffer.size ${requestBuffer.size}")
//        Logger().log("MonitaUploadWorker", "maxBatchSize ${maxBatchSize}")
//
//
//        // If the buffer has reached the maximum batch size, send the batch
//        if (requestBuffer.size >= maxBatchSize) {
//            sendBatch()
//
//        }
    }

    // Method to check and send the batch
    private suspend fun checkAndSendBatch() {
        val payloads = dao.getAll()
        if (payloads.size >= maxBatchSize) {
            sendBatch(payloads)
        }

        Logger().log("MonitaUploadWorker", "requestBuffer.size ${payloads.size}")
        Logger().log("MonitaUploadWorker", "maxBatchSize ${maxBatchSize}")
    }

    // Method to send the batch using WorkManager
    private suspend fun sendBatch(payloads: List<RequestPayloadEntity>) {


        // Convert the list of RequestPayloadEntity to JSON
        val requestDataJson = gson.toJson(payloads)

        // Create input data for WorkManager
        val data = Data.Builder()
            .putString("event_data", requestDataJson)
            .build()


        // Convert the list of RequestPayload to JSON
//        val requestDataJson = gson.toJson(requestBuffer)
//
//        // Create input data for WorkManager
//        val data = Data.Builder()
//            .putString("event_data", requestDataJson)
//            .build()

        // Create and enqueue the WorkRequest
        val uploadWorkRequest = OneTimeWorkRequestBuilder<MonitaUploadWorker>()
            .setInputData(data)
            .build()
        Logger().log("MonitaUploadWorker", "sending batch to server")


        WorkManager.getInstance(context).enqueue(uploadWorkRequest)

        // Clear the database after enqueuing the work
        withContext(Dispatchers.IO) {
            dao.clearAll()
        }
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

