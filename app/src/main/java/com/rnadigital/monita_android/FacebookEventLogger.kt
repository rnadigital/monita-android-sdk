package com.rnadigital.monita_android

import com.rnadigital.monita_android_sdk.OkHttpClientProvider
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException
import java.security.MessageDigest


class FacebookEventLogger {

    // Your Pixel ID and Access Token
    private val pixelId = "1360827538378982"
    private val accessToken = "YOUR_ACCESS_TOKEN"

    // Facebook Conversion API endpoint
    private val url = "https://graph.facebook.com/v13.0/$pixelId/events?access_token=$accessToken"

    // Send purchase event using OkHttp
    fun logPurchaseEvent() {
        // Build the JSON body with event data
        val jsonBody = JSONObject().apply {
            put("event_name", "Purchase")
            put(
                "event_time",
                (System.currentTimeMillis() / 1000).toString()
            )

            val userData = JSONObject().apply {
                put("email", hashData("example@domain.com"))
                put("phone", hashData("1234567890"))
            }
            put("user_data", userData)

            // Custom event data like purchase amount
            val customData = JSONObject().apply {
                put("currency", "USD")
                put("value", 29.99)
            }
            put("custom_data", customData)

            put("action_source", "app")
        }

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            jsonBody.toString()
        )

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()


        val client = OkHttpClientProvider.getClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    println("Event logged successfully: ${response.body?.string()}")
                } else {
                    println("Failed to log event: ${response.body?.string()}")
                }
            }
        })
    }

    private fun hashData(data: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(data.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

}
