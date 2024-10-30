package com.rnadigital.monita_android_sdk.roomDatabase


import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepository(context: Context) {
    private val dataDao = AppDatabase.getDatabase(context).dataDao()

    suspend fun saveData(eventType: String, payload: String) {
        val data = DataEntity(timestamp = System.currentTimeMillis(), eventType = eventType, payload = payload)
        dataDao.insert(data)
    }

    suspend fun fetchBatchData(limit: Int): List<DataEntity> {
        return dataDao.getAllData().take(limit)
    }

    suspend fun deleteProcessedData(limit: Int) {
        dataDao.deleteOldData(limit)
    }
}
