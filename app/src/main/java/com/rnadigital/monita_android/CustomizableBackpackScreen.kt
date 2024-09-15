package com.rnadigital.monita_android

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rnadigital.monita_android_sdk.OkHttpClientProvider
import com.rnadigital.monita_android_sdk.SDKInitializer
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.IOException

@Preview
@Composable
fun CustomizableBackpackScreen() {

    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Scrollable content inside a LazyColumn
        LazyColumn(
            modifier = Modifier
                .weight(1f) // Ensures LazyColumn takes the remaining space, leaving room for the button
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                // Back button and Heart/Favorite icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Handle Back */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    IconButton(onClick = { /* Handle Favorite */ }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite")
                    }
                }

                // Backpack Image Card
                Card(
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bag), // Replace with actual image
                        contentDescription = "Backpack",
                        modifier = Modifier
                            .height(500.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.FillBounds
                    )
                }

                // Title and Price
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    // Text aligned to the start (left)
                    Text(
                        text = "Customizable Everyday Backpack",
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .align(Alignment.Start) // Aligns text to the start (left) of the column
                            .padding(vertical = 4.dp)
                    )

                    // Text aligned to the center (horizontally)
                    Text(
                        text = "$89.99",
                        modifier = Modifier
                            .align(Alignment.Start) // Aligns text to the center horizontally
                            .padding(vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = "Our Customizable Everyday Backpack is designed for versatility, whether you're commuting to work, heading to class, or embarking on an adventure. This backpack offers multiple sizes, a range of vibrant colors, and materials to suit your lifestyle.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.Start) // Aligns text to the center horizontally
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Fixed button at the bottom
        Button(
            onClick = {
//                    Thread {
                        sendPurchaseEvent(context)
                        Log.d("SDKLogger", "init: makeNetworkRequestnew ")
//                    }.start()

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "Buy Now")
        }
    }
}


fun sendPurchaseEvent(context: Context) {

    val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
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
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "Purchase successful!", Toast.LENGTH_SHORT).show()
                }

            } else {
                println("Failed to send the event: ${response.message}")
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "Purchase failed: ${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    })


    logFirebaseEvent("purchase", Bundle().apply {
        putString("item_name", "phone")
        putString("currency", "USD")
        putDouble("value", 299.99)
    })


}

fun logFirebaseEvent(eventName: String, params: Bundle) {
//    val analyticsInterceptor = SDKInitializer.TrackingPlanSDK.analyticsInterceptor
//    if (analyticsInterceptor != null) {
//        analyticsInterceptor.logEvent(eventName, params)
//    } else {
//        Log.e("MainActivity", "FirebaseAnalyticsInterceptor not initialized!")
//    }
}



