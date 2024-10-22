package com.rnadigital.monita_android_sdk.utils

import android.os.Bundle
import androidx.annotation.NonNull
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.StandardCharsets


object JSONUtils {
    @Throws(JSONException::class)
    fun createPayload(name: String?, bundle: Bundle): JSONObject {
        val params = makeJSONObject(bundle)
        val payload = JSONObject()
        payload.put("method", name)
        payload.put("params", params)
        return payload
    }

    fun encodeJsonPayload(@NonNull payload: JSONObject): ByteArray {
        return payload.toString().toByteArray(StandardCharsets.UTF_8)
    }

    @Throws(JSONException::class)
    fun makeJSONArray(@NonNull array: Array<Any?>): JSONArray {
        val jsonArray = JSONArray()

        for (item in array) {
            if (item == null) {
                jsonArray.put(JSONObject.NULL)
            } else if (item is Bundle) {
                jsonArray.put(makeJSONObject(item))
            } else if (item.javaClass.isArray) {
                jsonArray.put(makeJSONArray(item as Array<Any?>))
            } else if (item is Iterable<*>) {
                jsonArray.put(makeJSONArray(item))
            } else {
                jsonArray.put(item)
            }
        }

        return jsonArray
    }

    @Throws(JSONException::class)
    fun makeJSONArray(@NonNull iterator: Iterable<*>): JSONArray {
        val jsonArray = JSONArray()

        for (item in iterator) {
            if (item == null) {
                jsonArray.put(JSONObject.NULL)
            } else if (item is Bundle) {
                jsonArray.put(makeJSONObject(item))
            } else if (item.javaClass.isArray) {
                jsonArray.put(makeJSONArray(item as Array<Any?>))
            } else if (item is Iterable<*>) {
                jsonArray.put(makeJSONArray(item))
            } else {
                jsonArray.put(item)
            }
        }

        return jsonArray
    }

    @Throws(JSONException::class)
    fun makeJSONObject(@NonNull bundle: Bundle): JSONObject {
        val jsonObject = JSONObject()

        for (key in bundle.keySet()) {
            val value = bundle[key]

            if (value == null) {
                jsonObject.put(key, JSONObject.NULL)
            } else if (value is Bundle) {
                jsonObject.put(key, makeJSONObject(value))
            } else if (value.javaClass.isArray) {
                jsonObject.put(key, makeJSONArray(value as Array<Any?>))
            } else if (value is Iterable<*>) {
                jsonObject.put(key, makeJSONArray(value))
            } else {
                jsonObject.put(key, value)
            }
        }

        return jsonObject
    }

    @Throws(JSONException::class)
    fun assign(@NonNull target: JSONObject, @NonNull source: JSONObject): JSONObject {
        val it = source.keys()
        while (it.hasNext()) {
            val key = it.next()
            target.put(key, source[key])
        }
        return target
    }
}
