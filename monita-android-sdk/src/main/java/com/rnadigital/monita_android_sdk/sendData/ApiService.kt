package com.rnadigital.monita_android_sdk.sendData

import android.util.Log
import com.google.gson.Gson
import com.rnadigital.monita_android_sdk.MonitaSDK
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Callback
import okhttp3.Response
import okhttp3.Call
import java.io.IOException

class ApiService {


    companion object {
        // JSON Media Type
        private val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        // Shared OkHttpClient instance for better performance and connection reuse
        private val client = HttpClient.client
    }
    // Function to send POST request with dynamic data
    fun postData(
        requestPayload: RequestPayload    // Dynamic payload content
    ) {
        val url = "https://dev-stream.getmonita.io/api/v1/"

        // Convert the payload to JSON string using Gson
        val gson = Gson()
        val jsonPayload = gson.toJson(requestPayload)

        // Create the request body
        val body = jsonPayload.toRequestBody(JSON)

        // Create POST request
        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .build()

        // Make the API call
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace() // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {

                    Log.d("SDKLogger", "Successfully posted data to Monita") // This is an example using Android's Logcat

                    println("intercepted : Successfully posted data to Monita! response.code ${response.code}")
                } else {
                    println("Failed to post data: ${response.message}")
                }
            }
        })
    }
}


object HttpClient {
    val client: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }
}