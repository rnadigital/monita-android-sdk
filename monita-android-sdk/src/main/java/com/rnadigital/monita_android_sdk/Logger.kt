package com.rnadigital.monita_android_sdk

import okhttp3.Request
import okhttp3.Response

class Logger {
    fun logRequest(request: Request) {
        // Log or process the request data
        println("Request URL: ${request.url}")
        println("Request Method: ${request.method}")
        println("Request Body: ${request.body}")
    }

    fun logResponse(response: Response) {
        // Log or process the response data
        println("Response Code: ${response.code}")
        println("Response Body: ${response.body?.string()}")
    }
}