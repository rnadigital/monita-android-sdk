package com.rnadigital.monita_android.ecoomerce

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductGridScreen(onProductClick: (Product) -> Unit) {
    val products = ProductRepository.getProducts()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Products") },
            )
        },
        content = { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(8.dp),
                content = {
                    items(products.size) { index ->
                        val product = products[index]
                        ProductCard(product = product,
                            onClick = { onProductClick(product) })

                    }
                }
            )
        }
    )
}
