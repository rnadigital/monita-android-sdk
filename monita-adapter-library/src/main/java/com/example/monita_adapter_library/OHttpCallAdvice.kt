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

        val buf = okio.Buffer()
        request.body?.writeTo(buf)
//        Log.d("AppXMLPostReq", "reqBody = ${buf.readUtf8()}")

        println("Intercepted OkHttpClient.newCall: request.body ${buf.readUtf8()}")

        SendDataToServer().uploadHttpData(request)
    }

    @JvmStatic
    @Advice.OnMethodExit
    fun onExit(@Advice.Return call: Call) {
        println("OkHttpClient.newCall has been called.")

    }
}
