package com.rnadigital.monita_android

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productId: String,
    productName: String,
    productPrice: String,
    productImageUrl: String,
    onAddToCartClick: (String, String, String) -> Unit
) {
    val context = LocalContext.current // For showing a Toast message

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // Pushes the button to the bottom
        ) {
            // Product Image

            Card(
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {

                Image(
                    painter = rememberImagePainter(data = productImageUrl),
                    contentDescription = "Backpack",
                    modifier = Modifier
                        .height(500.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillBounds
                )
            }


            // Product Details
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Product ID: $productId", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Product Name: $productName", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Price: $productPrice", style = MaterialTheme.typography.titleMedium)
            }

            // "Add to Cart" Button
            Button(
                onClick = {
                    onAddToCartClick(productId, productName, productPrice)

                    Toast.makeText(
                        context,
                        "$productName added to cart!",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = "Add to Cart")
            }
        }
    }
}
