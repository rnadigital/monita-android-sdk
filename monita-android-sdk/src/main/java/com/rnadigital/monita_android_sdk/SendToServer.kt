package com.rnadigital.monita_android_sdk

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rnadigital.monita_android_sdk.MonitaSDK.monitoringConfig
import com.rnadigital.monita_android_sdk.monitoringConfig.FilterValidator
import com.rnadigital.monita_android_sdk.monitoringConfig.FilterValidator.findValueByKey
import com.rnadigital.monita_android_sdk.monitoringConfig.Vendor
import com.rnadigital.monita_android_sdk.sendData.ApiService
import com.rnadigital.monita_android_sdk.utils.JSONUtils.createPayload
import com.rnadigital.monita_android_sdk.utils.JSONUtils.encodeJsonPayload
import okhttp3.Request
import okio.Buffer


class SendToServer(
) {
    private val apiService = ApiService()
    private val logger: Logger = Logger()


    fun vendorUrlMatched(url: String): Vendor? {
        monitoringConfig.vendors.forEach { vendor ->
            vendor.urlPatternMatches.forEach { pattern ->
                if (url.contains(pattern)) {
                    logger.log("Matched vendor: ${vendor.vendorName} with URL pattern: $pattern")
                    return vendor // Exit loop on match
                }
            }
        }
        return null
    }


    fun bundleToString(bundle: Bundle): String {
        val keys = bundle.keySet()
        val stringBuilder = StringBuilder()
        for (key in keys) {
            stringBuilder.append(key).append("=").append(bundle.get(key)).append(", ")
        }
        return stringBuilder.toString().trimEnd(',', ' ')
    }

    fun createPayloadAsListOfMaps(name: String?, bundle: Bundle): List<Map<String, Any>> {
        // Create the payload as a JSONObject
        val payload = createPayload(name, bundle)

        // Convert the JSONObject into a Map and then into a List<Map<String, Any>>
        val dtData = mutableListOf<Map<String, Any>>()
        payload.keys().forEach { key ->
            val map = mapOf(key to payload.get(key))
            dtData.add(map)
        }
        return dtData
    }

    fun bundleToListOfMaps(bundle: Bundle): List<Map<String, Any>> {
        val dtData = mutableListOf<Map<String, Any>>()
        for (key in bundle.keySet()) {
            val map = mapOf(key to bundle.get(key))
            dtData.add(map as Map<String, Any>)
        }
        return dtData
    }

    fun findeventParameterValue(dtData: List<Map<String, Any>>, key: String): String {
        dtData.forEach { map ->
            // Check if the map contains the key
            if (map.containsKey(key)) {
                // Get the value associated with the key and return it as a string
                return map[key].toString()
            }
        }
        // Return an empty string if the key is not found in any map
        return ""
    }




    fun getDtData(requestBodyString: String): Map<String, Any> {
        val gson = Gson()
        val jsonMapType = object : TypeToken<Map<String, Any>>() {}.type
        return gson.fromJson(requestBodyString, jsonMapType)
    }

    fun createAdobeAnalyticsMonitaData( name: String, params: Bundle) {

        println("Intercepted createAdobeAnalyticsMonitaData")


        val url = "b/ss"


        val vendor = vendorUrlMatched(url)
        if (vendorUrlMatched(url) != null) {

//            val requestBodyMap: Map<String, Any> = getDtData(bundleToString(params))
            var dtData = createPayloadAsListOfMaps(name, params)
            val eventParameterValue =
                vendor?.eventParamter?.let { findeventParameterValue(dtData, it) }



            val payload = encodeJsonPayload(createPayload(name, params))
            logger.log("vendors.eventParamter: ${vendor?.eventParamter}")
            logger.log("vendors.vendorName: ${vendor?.vendorName}")
            logger.log("request.method: pos")
            logger.log("request.url: $url")
            val isValid = vendor?.filters?.let {
                println("Intercepted vendor.filters $it")
                FilterValidator.validateFilters(dtData, it)
            }


           dtData = vendor?.execludeParameters?.let { FilterValidator.excludeParameters(dtData, it) }!!
           println("newDtData $dtData")

            if (isValid == true){
                sendToMonita(
                    vendorEvent = eventParameterValue ?: "",
                    vendorName = vendor?.vendorName ?: "",
                    httpMethod = "Post",
                    capturedUrl = url,
                    dtData = dtData
                )
            }

        }


    }



    fun createGoogleAdsMonitaData( name: String, params: Bundle) {

        println("Intercepted createGoogleAdsMonitaData")


        val url = "googleadservices.com/pagead/conversion/"


        val vendor = vendorUrlMatched(url)
        if (vendorUrlMatched(url) != null) {

//            val requestBodyMap: Map<String, Any> = getDtData(bundleToString(params))
            var dtData = createPayloadAsListOfMaps(name, params)
            val eventParameterValue =
                vendor?.eventParamter?.let { findeventParameterValue(dtData, it) }



            val payload = encodeJsonPayload(createPayload(name, params))
            logger.log("vendors.eventParamter: ${vendor?.eventParamter}")
            logger.log("vendors.vendorName: ${vendor?.vendorName}")
            logger.log("request.method: pos")
            logger.log("request.url: $url")

            val isValid = vendor?.filters?.let {
                println("Intercepted vendor.filters $it")
                FilterValidator.validateFilters(dtData, it)
            }

            dtData = vendor?.execludeParameters?.let { FilterValidator.excludeParameters(dtData, it) }!!
            println("newDtData $dtData")

            if (isValid == true){
                sendToMonita(
                    vendorEvent = eventParameterValue ?: "",
                    vendorName = vendor?.vendorName ?: "",
                    httpMethod = "Post",
                    capturedUrl = url,
                    dtData = dtData
                )
            }

        }


    }




    fun createFacebookMonitaData( name: String, params: Bundle) {
        println("Intercepted createGoogleAdsMonitaData")


        val url = "facebook.com/tr/"


        val vendor = vendorUrlMatched(url)
        if (vendorUrlMatched(url) != null) {

//            val requestBodyMap: Map<String, Any> = getDtData(bundleToString(params))
            var dtData = createPayloadAsListOfMaps(name, params)
            val eventParameterValue =
                vendor?.eventParamter?.let { findeventParameterValue(dtData, it) }



            val payload = encodeJsonPayload(createPayload(name, params))
            logger.log("vendors.eventParamter: ${vendor?.eventParamter}")
            logger.log("vendors.vendorName: ${vendor?.vendorName}")
            logger.log("request.method: pos")
            logger.log("request.url: $url")
            val isValid = vendor?.filters?.let {
                println("Intercepted vendor.filters $it")
                FilterValidator.validateFilters(dtData, it)
            }

            dtData = vendor?.execludeParameters?.let { FilterValidator.excludeParameters(dtData, it) }!!
            println("newDtData $dtData")

            if (isValid == true){
                sendToMonita(
                    vendorEvent = eventParameterValue ?: "",
                    vendorName = vendor?.vendorName ?: "",
                    httpMethod = "Post",
                    capturedUrl = url,
                    dtData = dtData
                )
            }

        }


    }

    fun createFirebaseMonitaData(fa: FirebaseAnalytics, name: String, params: Bundle) {

        println("Intercepted createFirebaseMonitaData")


        val url = "firebase.googleapis.com"


        val vendor = vendorUrlMatched(url)
        if (vendorUrlMatched(url) != null) {

//            val requestBodyMap: Map<String, Any> = getDtData(bundleToString(params))
            var dtData = createPayloadAsListOfMaps(name, params)
            val eventParameterValue =
                vendor?.eventParamter?.let { findeventParameterValue(dtData, it) }


            val payload = encodeJsonPayload(createPayload(name, params))
            logger.log("vendors.eventParamter: ${vendor?.eventParamter}")
            logger.log("vendors.vendorName: ${vendor?.vendorName}")
            logger.log("request.method: pos")
            logger.log("request.url: $url")

            val isValid = vendor?.filters?.let {
                println("Intercepted vendor.filters $it")
                FilterValidator.validateFilters(dtData, it)
            }

            dtData = vendor?.execludeParameters?.let { FilterValidator.excludeParameters(dtData, it) }!!
            println("newDtData $dtData")

            if (isValid == true) {

                sendToMonita(
                    vendorEvent = eventParameterValue ?: "",
                    vendorName = vendor?.vendorName ?: "",
                    httpMethod = "Post",
                    capturedUrl = url,
                    dtData = dtData
                )

            }
        }


    }

    fun createHTTPMonitaData(request: Request) {

        println("Intercepted createHTTPMonitaData")


        val url = request.url.toString()

        val vendor = vendorUrlMatched(url)
        if (vendorUrlMatched(url) != null) {


            val requestBodyString = request.body?.let { body ->
                Buffer().apply { body.writeTo(this) }.readUtf8()
            } ?: ""

            val requestBodyMap: Map<String, Any> = getDtData(requestBodyString)
            var dtData = listOf(requestBodyMap)
            val eventParameterValue =   vendor?.eventParamter?.let { searchKeyInList(dtData, it) }
            println("Intercepted vendor?.eventParamter ${vendor?.eventParamter}")

            println("Intercepted eventParameterValue $eventParameterValue")


            logger.log("vendors.eventParamter: ${vendor?.eventParamter}")
            logger.log("vendors.vendorName: ${vendor?.vendorName}")
            logger.log("request.method: ${request.method}")
            logger.log("request.url: ${request.url}")
            val isValid = vendor?.filters?.let {
                println("Intercepted vendor.filters $it")
                FilterValidator.validateFilters(dtData, it)
            }

            println("Intercepted vendor.filters $isValid")
            dtData = vendor?.execludeParameters?.let { FilterValidator.excludeParameters(dtData, it) }!!
            println("newDtData $dtData")

            if (isValid == true) {

                sendToMonita(
                    vendorEvent = eventParameterValue ?: "",
                    vendorName = vendor?.vendorName ?: "",
                    httpMethod = request.method ?: "",
                    capturedUrl = request.url.toString(),
                    dtData = dtData
                )

            }


        }
    }

    fun searchKeyInList(dataList: List<Map<String, Any>>, key: String): String {
        for (map in dataList) {

            val value = findValueByKey(map, key).toString()

            println("searchKeyInList map $map has key $key")
                println("searchKeyInList map[key].toString() ${map[key].toString()}")

                return value // Return the value if key exists

        }
        return "" // Return an empty string if the key is not found
    }


    private fun sendToMonita(
        vendorEvent: String,         // Vendor event
        vendorName: String,          // Vendor name (case-sensitive)
        httpMethod: String,          // HTTP method (POST)
        capturedUrl: String,
        dtData: List<Map<String, Any>>
    ) {

        apiService.postData(
            vendorEvent = vendorEvent,
            vendorName = vendorName,
            httpMethod = httpMethod,
            capturedUrl = capturedUrl,
            dtData = dtData
        )
    }
}
