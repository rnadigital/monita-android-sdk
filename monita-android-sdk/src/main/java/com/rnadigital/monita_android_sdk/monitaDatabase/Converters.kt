package com.rnadigital.monita_android_sdk.monitaDatabase

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromListMapToJson(list: List<Map<String, Any>>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromJsonToListMap(jsonString: String): List<Map<String, Any>> {
        val type = object : TypeToken<List<Map<String, Any>>>() {}.type
        return Gson().fromJson(jsonString, type)
    }
}
