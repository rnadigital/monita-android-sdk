package com.rnadigital.monita_android_sdk.OkHttp
import com.rnadigital.monita_android_sdk.MonitaSDK
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import okio.IOException

class MonitaOkHttpCallback(
    private val originalCallback: Callback,
) : Callback {

    override fun onFailure(call: Call, e: IOException) {
        val request = call.request()
        if (request != null) {
            val url = request.url.toString()
            val method = request.method
        }
        originalCallback.onFailure(call, e)
    }

    override fun onResponse(call: Call, response: Response) {
        originalCallback.onResponse(call, response)
    }
}
