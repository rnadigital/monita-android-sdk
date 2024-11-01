package com.example.monita_adapter_library

import com.example.SendDataToServer
import com.rnadigital.monita_android_sdk.Logger
import net.bytebuddy.asm.Advice
import okhttp3.Call
import okhttp3.Request

object OHttpCallAdvice {

    @JvmStatic
    @Advice.OnMethodEnter
    fun onEnter(@Advice.Argument(0) request: Request) {
        Logger().log("Intercepted OkHttpClient.newCall:request.url  ${request.url}")

        val buf = okio.Buffer()
        request.body?.writeTo(buf)
//        Logger().log("AppXMLPostReq", "reqBody = ${buf.readUtf8()}")

        Logger().log("Intercepted OkHttpClient.newCall: request.body ${buf.readUtf8()}")

        SendDataToServer().uploadHttpData(request)
    }

    @JvmStatic
    @Advice.OnMethodExit
    fun onExit(@Advice.Return call: Call) {
        Logger().log("OkHttpClient.newCall has been called.")

    }
}
