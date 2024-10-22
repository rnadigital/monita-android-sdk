package com.example.monita_adapter_library

import com.example.SendDataToServer
import net.bytebuddy.asm.Advice
import okhttp3.Call
import okhttp3.Request

object OHttpCallAdvice {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(@Advice.Argument(0) request: Request) {
        println("Intercepted OkHttpClient.newCall:request.url  ${request.url}")
        println("Intercepted OkHttpClient.newCall: request.method ${request.method}")

        SendDataToServer().uploadHttpData(request)
    }

    @JvmStatic
    @Advice.OnMethodExit
    fun onExit(@Advice.Return call: Call) {
        println("OkHttpClient.newCall has been called.")

    }
}
