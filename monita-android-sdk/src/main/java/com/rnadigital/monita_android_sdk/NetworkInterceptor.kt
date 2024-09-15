package com.rnadigital.monita_android_sdk

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rnadigital.monita_android_sdk.monitoringConfig.MonitoringConfig
import com.rnadigital.monita_android_sdk.monitoringConfig.Vendor
import com.rnadigital.monita_android_sdk.sendData.ApiService
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException

class NetworkInterceptor(
    private val logger: Logger,
    private val monitoringConfig: MonitoringConfig
) : Interceptor {


    private val apiService = ApiService() // Initialize ApiService


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


        // Check if the URL matches any pattern in the configuration
        monitoringConfig.vendors.forEach { vendor ->
            vendor.urlPatternMatches.forEach { pattern ->

                if (url.contains(pattern)) {
                    logger.log("Matched vendor: ${vendor.vendorName} with URL pattern: $pattern")

                    sendToMonita(request, vendor)
                    // Apply further logic if needed
                } else {
//                    logger.log("did not Matched vendor: ${vendor.vendorName} with URL pattern: $pattern")

                }
            }
        }

        val response = chain.proceed(request)
        logger.logResponse(response)

        return response
    }


    fun sendToMonita(request: Request, vendors: Vendor) {

        val randomId = (1..1000000000).random()


        val requestBody = request.body
        val buffer = Buffer()
        requestBody?.writeTo(buffer)
        val requestBodyString = buffer.readUtf8()


        val gson = Gson()
        val jsonMapType = object : TypeToken<Map<String, Any>>() {}.type
        val requestBodyMap: Map<String, Any> = gson.fromJson(requestBodyString, jsonMapType)

        // Wrap the request body in a list to form a JSON array
        val dtData = listOf(requestBodyMap)

        logger.log("vendors.eventParamter " + vendors.eventParamter)
        logger.log("vendors.vendorName " + vendors.vendorName)
        logger.log("request.method " + request.method)
        logger.log("request.url " + request.url)

        var eventParameterValue: String? = null
        dtData.forEach { dataMap ->
            if (vendors.eventParamter in dataMap) {
                eventParameterValue = dataMap[monitoringConfig.vendors.first().eventParamter]?.toString()
            }
        }

        apiService.postData(
            token = "fe041147-0600-48ad-a04e-d3265becc4eb", // Use relevant token
            sdkVersion = "1.0", // SDK version
            appVersion = "1.0", // App version
            vendorEvent = eventParameterValue ?: "", // Event value
            vendorName = vendors.vendorName, // Vendor name
            httpMethod = request.method, // HTTP method (POST, GET, etc.)
            capturedUrl = request.url.toString(), // Captured URL
            appId = "com.rnadigital.monita_android", // App ID
            sessionId = "", // Session ID
            consentString = "GRANTED", // Consent value
            hostAppVersion = "com.rnadigital.monita_android", // Host app version
            dtData = dtData // Dynamic data payload
        )
    }
}