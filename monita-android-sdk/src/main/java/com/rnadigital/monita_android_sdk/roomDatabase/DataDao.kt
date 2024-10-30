package com.rnadigital.monita_android_sdk.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: DataEntity)

    @Query("SELECT * FROM data_table")
    suspend fun getAllData(): List<DataEntity>

    @Query("DELETE FROM data_table WHERE id IN (SELECT id FROM data_table LIMIT :limit)")
    suspend fun deleteOldData(limit: Int): Int
}
