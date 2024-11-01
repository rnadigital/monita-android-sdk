package com.example.monita_adapter_library

import com.rnadigital.monita_android_sdk.Logger
import net.bytebuddy.implementation.bind.annotation.AllArguments
import net.bytebuddy.implementation.bind.annotation.Origin
import net.bytebuddy.implementation.bind.annotation.RuntimeType
import net.bytebuddy.implementation.bind.annotation.SuperCall
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Method
import java.util.concurrent.Callable

object NewCallInterceptor {

    @RuntimeType
    @JvmStatic
    fun intercept(
        @AllArguments args: Array<Any>,
        @SuperCall originalMethod: Callable<Call>,
        @Origin method: Method
    ): Call {
        // Extract the Request object from the arguments
        val request = args[0] as Request
        Logger().log("Intercepting OkHttpClient.newCall: ${request.url}")

        // Add custom logic before calling the original newCall method
        // For example, you can modify the request here if needed

        // Proceed with the original method
        val call = originalMethod.call()

        // Add custom logic after calling the original method if needed
        return call
    }
}
