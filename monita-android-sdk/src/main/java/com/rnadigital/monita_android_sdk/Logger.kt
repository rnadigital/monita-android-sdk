package com.rnadigital.monita_android_sdk

import android.util.Log
import okhttp3.Request
import okhttp3.Response

class Logger {

    fun log(message: String) {
        // Here you can define how to log the message (e.g., using Android Logcat, or saving to a file)
        if (MonitaSDK.isLoggerEnabled()) Log.d("Monita SDK Logger", message)
    }

    fun log(tittle: String, message: String) {
        // Here you can define how to log the message (e.g., using Android Logcat, or saving to a file)
        if (MonitaSDK.isLoggerEnabled()) Log.d("Monita SDK Logger", "$tittle - $message")
    }

    fun logRequest(request: Request) {
        // Log or process the request data
            log("Request URL: ${request.url}")
            log("Request Method: ${request.method}")
            log("Request Body: ${request.body?.toString()}")

    }

    fun logResponse(response: Response) {
        // Log or process the response data
            log("Response Code: ${response.code}")
            log("Response Body: ${response.body?.string()}")

    }

    fun error(message: String) {
        if (MonitaSDK.isLoggerEnabled()) Log.e("Monita SDK Logger", message)
    }
}