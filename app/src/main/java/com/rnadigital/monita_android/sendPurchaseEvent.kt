package com.rnadigital.monita_android

import android.content.Context
import android.os.Bundle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun sendPurchaseEvent(context: Context) {
    val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
    val client = OkHttpClient.Builder().build()
    val randomId = (1..1000).random()

    // URL for the Firebase endpoint
    val url = "https://authentication-5c281.firebaseio.com/ProductPurchase/$randomId.json" // Notice ".json" for Firebase DB

    // Payload for the POST request
    val payload = """
    {
        "event": "purchase",
        "commerce": {
            "items": [
                {
                    "itemNumber": "ABC123",
                    "itemName": "Adidas Shoes",
                    "quantity": 1
                }
            ]
        }
    }
    """.trimIndent()

    // Create request body with JSON payload
    val body = RequestBody.create(JSON, payload)

    // Create POST request
    val request = Request.Builder()
        .url(url)
        .post(body)
        .build()

    // Make asynchronous network call using coroutines
    try {
        val response = client.newCall(request).await()
        if (response.isSuccessful) {
            println("Successfully sent the OKHttp purchase event!")
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Purchase successful!", Toast.LENGTH_SHORT).show()
            }
        } else {
            println("Failed to send the OKHttp event: ${response.message}")
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Purchase failed: ${response.message}", Toast.LENGTH_SHORT).show()
            }
        }
    } catch (e: Exception) {
        println("Error sending purchase OKHttp event: ${e.message}")
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

// Extension function to await the OkHttp response in a coroutine-friendly way
suspend fun Call.await(): Response = suspendCancellableCoroutine { continuation ->
    enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            if (continuation.isCancelled) return
            continuation.resumeWithException(e)
        }

        override fun onResponse(call: Call, response: Response) {
            if (continuation.isCancelled) {
                response.close()
                return
            }
            continuation.resume(response)
        }
    })

    continuation.invokeOnCancellation {
        cancel()
    }
}


suspend fun logFirebaseEvent(fa: FirebaseAnalytics, eventName: String, params: Bundle) {
    fa.logEvent(eventName, params)
}