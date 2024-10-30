package com.rnadigital.monita_android_sdk.roomDatabase

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.rnadigital.monita_android_sdk.worker.MonitaUploadWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataBatchManager(private val context: Context) {
    private val dataRepository = DataRepository(context)
    private val workManager = WorkManager.getInstance(context)

    fun collectData(eventType: String, payload: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dataRepository.saveData(eventType, payload)

            val dataCount = dataRepository.fetchBatchData(Int.MAX_VALUE).size
            if (dataCount >= 10) {
                val uploadRequest = OneTimeWorkRequestBuilder<MonitaUploadWorker>().build()
                workManager.enqueue(uploadRequest)
            }
        }
    }
}
