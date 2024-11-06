package com.rnadigital.monita_android_sdk.monitaDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RequestPayloadDao {
    @Insert
    suspend fun insert(payload: RequestPayloadEntity)

    @Query("SELECT * FROM request_payloads")
    suspend fun getAll(): List<RequestPayloadEntity>

    @Query("DELETE FROM request_payloads")
    suspend fun clearAll()
}
