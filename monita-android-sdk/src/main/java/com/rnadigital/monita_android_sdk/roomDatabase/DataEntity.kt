package com.rnadigital.monita_android_sdk.roomDatabase


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val eventType: String,
    val payload: String
)
