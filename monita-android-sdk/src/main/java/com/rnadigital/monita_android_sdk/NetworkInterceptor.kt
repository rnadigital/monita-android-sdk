package com.rnadigital.monita_android_sdk

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class NetworkInterceptor(private val logger: Logger) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        logger.logRequest(request)

        val response = chain.proceed(request)
        logger.logResponse(response)

        return response
    }
}