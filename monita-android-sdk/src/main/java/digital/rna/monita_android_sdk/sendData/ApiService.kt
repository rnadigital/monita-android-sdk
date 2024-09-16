package digital.rna.monita_android_sdk.sendData

import android.util.Log
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Callback
import okhttp3.Response
import okhttp3.Call
import java.io.IOException

class ApiService {


    // JSON Media Type
    private val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
    private val client = OkHttpClient()

    // Function to send POST request with dynamic data
    fun postData(
        token: String,               // User-provided token
        sdkVersion: String,          // SDK version
        appVersion: String,          // App version
        vendorEvent: String,         // Vendor event
        vendorName: String,          // Vendor name (case-sensitive)
        httpMethod: String,          // HTTP method (POST)
        capturedUrl: String,         // Captured HTTP endpoint URL
        appId: String,               // App ID
        sessionId: String,           // Session ID
        consentString: String,       // Consent string value
        hostAppVersion: String,      // Host app version
        dtData: List<Map<String, Any>>         // Dynamic payload content
    ) {
        val url = "https://dev-stream.getmonita.io/api/v1/"

        // Create the payload with dynamic data
        val payload = RequestPayload(
            t = token,
            dm = "app", // Deployment method (app for SDK based deployments)
            mv = sdkVersion,
            sv = appVersion,
            tm = System.currentTimeMillis().toDouble() / 1000.0, // Unix time in seconds with milliseconds
            e = vendorEvent,
            vn = vendorName,
            st = "", // HTTP call status, can be success/failed (for now it's null)
            m = httpMethod,
            vu = capturedUrl,
            u = appId,
            p = "",  // App area or null
            dt = dtData, // Dynamic payload data
            rl = sdkVersion, // SDK release version
            doHost = hostAppVersion,
            cn = consentString,
            sid = sessionId,
            cid = "" // Customer ID (null or SDK generated)
        )

        // Convert the payload to JSON string using Gson
        val gson = Gson()
        val jsonPayload = gson.toJson(payload)

        // Create the request body
        val body = jsonPayload.toRequestBody(JSON)

        // Create POST request
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        // Make the API call
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace() // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {

                    Log.d("SDKLogger", "Successfully posted data to Monita") // This is an example using Android's Logcat

                    println("Successfully posted data to Monita!")
                } else {
                    println("Failed to post data: ${response.message}")
                }
            }
        })
    }
}