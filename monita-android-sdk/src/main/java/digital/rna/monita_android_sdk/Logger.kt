package digital.rna.monita_android_sdk

import android.util.Log
import okhttp3.Request
import okhttp3.Response

class Logger {

    fun log(message: String) {
        // Here you can define how to log the message (e.g., using Android Logcat, or saving to a file)
        Log.d("SDKLogger", message) // This is an example using Android's Logcat
    }
    fun logRequest(request: Request) {
        // Log or process the request data


        println("Request URL: ${request.url}")
        println("Request Method: ${request.method}")
//        println("Request Body: ${request.body?.toString()}")
    }

    fun logResponse(response: Response) {
        // Log or process the response data
        println("Response Code: ${response.code}")
        println("Response Body: ${response.body?.string()}")
    }
}