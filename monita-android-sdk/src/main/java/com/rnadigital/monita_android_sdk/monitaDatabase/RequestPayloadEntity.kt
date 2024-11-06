package com.rnadigital.monita_android_sdk.monitaDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "request_payloads")
data class RequestPayloadEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val t: String,
    val dm: String,
    val mv: String,
    val sv: String,
    val tm: Double,
    val e: String,
    val vn: String,
    val st: String,
    val m: String,
    val vu: String,
    val u: String,
    val p: String,
    val dt: List<Map<String, Any>>, // Update `dt` to be a Map instead of String
    val rl: String,
    val domain: String,
    val cn: String,
    val sid: String,
    val cid: String
)
