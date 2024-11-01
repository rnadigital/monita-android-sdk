package com.rnadigital.monita_android

import android.util.Log
import com.rnadigital.monita_android_sdk.Logger
import com.rnadigital.monita_android_sdk.NetworkInterceptor
import net.bytebuddy.implementation.bind.annotation.Argument
import net.bytebuddy.implementation.bind.annotation.SuperCall
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.Callable

class OkHttpInterceptor {

    companion object {
        @JvmStatic
        fun intercept(
            @SuperCall originalCall: Callable<Call>,
            @Argument(0) request: Request
        ): Call {
            // Log or modify the request here
            Log.d("OkHttpInterceptor", "Intercept method request: $request")

            val client = OkHttpClient.Builder()
                .addInterceptor(NetworkInterceptor())
                .build()

            // Call the original method with the modified request
//            return originalCall.call()
            return client.newCall(request)
        }
    }
}


    // Custom class to wrap the `Call` and intercept the `enqueue` method.
    class InterceptedCall(private val originalCall: Call) : Call by originalCall {
        override fun enqueue(responseCallback: Callback) {
            val wrappedCallback = object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    // Intercept the response here
                    Log.d("android App","Intercepted response: ${response.code}")
                    responseCallback.onResponse(call, response)
                }

                override fun onFailure(call: Call, e: IOException) {
                    // Intercept the error here
                    Log.d("android App","Intercepted failure: ${e.message}")
                    responseCallback.onFailure(call, e)
                }
            }

            // Call the original enqueue method with the wrapped callback
            originalCall.enqueue(wrappedCallback)
        }
    }



