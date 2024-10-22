package com.rnadigital.monita_android_sdk.OkHttp
import com.rnadigital.monita_android_sdk.Logger
import com.rnadigital.monita_android_sdk.MonitaSDK
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import okio.IOException

object MonitaOkHttpClient {

    fun execute(call: Call): Response {
        val response: Response
        try {
            response = call.execute()
            trackRequest(call.request(), response)
            Logger().log("execute")

        } catch (e: IOException) {
            trackRequestFailure(call.request(), e)
            throw e
        }
        return response
    }

    fun enqueue(call: Call, callback: Callback) {
        val monitoredCallback = MonitaOkHttpCallback(callback)
        call.enqueue(monitoredCallback)
        Logger().log("enqueue")

    }

    private fun trackRequest(request: Request, response: Response) {
        val url = request.url.toString()
        val method = request.method
        val responseCode = response.code

        Logger().log("trackRequest")

        Logger().logRequest(request)
        Logger().logResponse(response)

    }

    private fun trackRequestFailure(request: Request, e: IOException) {
        val url = request.url.toString()
        val method = request.method
        Logger().log("trackRequestFailure")

    }
}
