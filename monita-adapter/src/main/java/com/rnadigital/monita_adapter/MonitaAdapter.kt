package com.rnadigital.monita_adapter

import android.content.Context
import android.util.Log
import com.rnadigital.monita_android_sdk.Logger
import com.rnadigital.monita_android_sdk.MonitaSDK
import com.rnadigital.monita_android_sdk.NetworkInterceptor
import com.rnadigital.monita_android_sdk.OkHttp.MonitaOkHttpClient
import com.rnadigital.monita_android_sdk.OkHttpClientProvider
import net.bytebuddy.ByteBuddy
import net.bytebuddy.android.AndroidClassLoadingStrategy
import net.bytebuddy.description.method.MethodDescription
import net.bytebuddy.dynamic.ClassFileLocator
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy
import net.bytebuddy.implementation.FixedValue
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.implementation.bind.annotation.Argument
import net.bytebuddy.implementation.bind.annotation.RuntimeType
import net.bytebuddy.implementation.bind.annotation.This
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.matcher.ElementMatchers.isFinal
import net.bytebuddy.matcher.ElementMatchers.named
import net.bytebuddy.matcher.ElementMatchers.not
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

object MonitaAdapter {
    private var token: String? = null

    fun install(context: Context, token: String) {
        this.token = token
        Logger().log( "MonitaAdapter installed")
        setupTrackingplanByteBuddy(context)
    }

    fun setupOkHttpClientByteBuddy(context: Context) {
        val strategy = AndroidClassLoadingStrategy.Wrapping(
            context.getDir("generated", Context.MODE_PRIVATE)
        )

        ByteBuddy()
            .redefine(OkHttpClient::class.java)
            .method(ElementMatchers.named<MethodDescription?>("okhttp3.OkHttpClient")
                .and(not(named("-deprecated_authenticator")))  // Exclude the deprecated method
                .and(not(isFinal())) ) // Exclude final methods)
            .intercept(MethodDelegation.to(OkHttpClientInterceptor::class.java))
            .make()
            .load(OkHttpClient::class.java.classLoader, strategy)
    }

    fun getToken(): String {
        return token ?: throw IllegalStateException("MonitaAdapter is not initialized with token")
    }
}



object OkHttpClientInterceptor {
    @JvmStatic
    fun getClientBuilder(): OkHttpClient {

        val client = OkHttpClient.Builder()
            .addInterceptor(NetworkInterceptor())
            .build()

        OkHttpClientProvider.setClient(client)

        return OkHttpClientProvider.getClient()
    }
}

// Interceptor class to delegate OkHttpClient's call methods to TrackingplanOkHttpClient
object OkHttpInterceptor {
    @JvmStatic
    fun execute(call: Call): Response {
        // Redirect the call to TrackingplanOkHttpClient's execute method
        Logger().log(" OkHttpInterceptor execute")


        return MonitaOkHttpClient.execute(call)
    }

    @JvmStatic
    fun enqueue(call: Call, callback: Callback) {
        // Redirect the call to TrackingplanOkHttpClient's enqueue method
        Logger().log(" OkHttpInterceptor enqueue")

        MonitaOkHttpClient.enqueue(call, callback)
    }
}

fun setupTrackingplanByteBuddy(context: Context) {

    Logger().log( "setup Trackingplan ByteBuddy")



    val strategy = AndroidClassLoadingStrategy.Wrapping(
        context.getDir("generated", Context.MODE_PRIVATE)
    )

    // Intercept the execute method of Call
    ByteBuddy()
        .subclass(Call::class.java)
        .method(named("execute"))
        .intercept(MethodDelegation.to(OkHttpInterceptor::class.java, "execute"))
        .make()
        .load(context::class.java.classLoader, strategy)

    // Intercept the enqueue method of Call
    ByteBuddy()
        .subclass(Call::class.java)
        .method(named("enqueue"))
        .intercept(MethodDelegation.to(OkHttpInterceptor::class.java, "enqueue"))
        .make()
        .load(context::class.java.classLoader, strategy)
}

