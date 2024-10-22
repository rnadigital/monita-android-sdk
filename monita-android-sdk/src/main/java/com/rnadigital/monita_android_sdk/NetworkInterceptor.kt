package com.rnadigital.monita_android_sdk

import com.rnadigital.monita_android_sdk.monitoringConfig.MonitoringConfig
import com.rnadigital.monita_android_sdk.sendData.ApiService
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException

class NetworkInterceptor() : Interceptor {


    private val apiService = ApiService() // Initialize ApiService
 val logger = Logger()


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        logger.logRequest(request)


        if (request.body != null) {
            val requestBody = request.body
            val buffer = Buffer()
            requestBody?.writeTo(buffer)
            val requestBodyString = buffer.readUtf8()

            // Log the body in JSON format (if it's JSON)
            logger.log("Request Body (JSON): $requestBodyString")
        }

//        SendToServer(request)

        val response = chain.proceed(request)
        logger.logResponse(response)

        return response
    }



}