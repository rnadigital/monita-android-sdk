package com.rnadigital.monita_android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rnadigital.monita_android.ui.theme.Monita_androidTheme
import com.rnadigital.monita_android_sdk.Logger
import com.rnadigital.monita_android_sdk.NetworkInterceptor
import com.rnadigital.monita_android_sdk.OkHttpClientProvider
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.IOException

class MainActivity : ComponentActivity() {

    val token = "fe041147-0600-48ad-a04e-d3265becc4eb"
    val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        com.rnadigital.monita_android_sdk.SDKInitializer.init ("token"){}

        setContent {
            Monita_androidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CustomizableBackpackScreen()
                }
            }
        }
    }



    private fun makeNetworkRequest() {
        val client = OkHttpClientProvider.getClient()
        val request = Request.Builder()
            .url("https://dummy.restapiexample.com/api/v1/employees")
            .build()

        client?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
//                    val responseData = response.body?.string()
                    // Handle the response data
                }
            }
        })
    }

    private fun makeNetworkRequestnew() {
        val client = OkHttpClientProvider.getClient()
        val request = Request.Builder()
            .url("https://storage.googleapis.com/cdn-monita-dev/custom-config/fe041147-0600-48ad-a04e-d3265becc4eb.json")
            .build()

        client?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
//                    println(response.body?.string())

//                    val responseData = response.body?.string()
                    // Handle the response data
                }
            }
        })
    }


    fun sendPurchaseEvent() {
        val client = OkHttpClientProvider.getClient()

        val randomId = (1..1000).random()

//       val url = "https://firestore.googleapis.com/v1/projects/authentication-5c281/databases/ProductPurchase/$randomId.json"


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

        // Make asynchronous network call
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle request failure
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle the response
                if (response.isSuccessful) {
                    println("Successfully sent the purchase event!")
                } else {
                    println("Failed to send the event: ${response.message}")
                }
            }
        })
    }




    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        Monita_androidTheme {
            Greeting("Android")
        }
    }

}