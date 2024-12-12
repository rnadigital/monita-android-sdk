package com.rnadigital.monita_android.ecoomerce

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rnadigital.monita_android.ui.theme.ComposeGridTheme

@Composable
fun ProductGridPreview(onProductClick: (Product) -> Unit) {
    ComposeGridTheme {
        ProductGridScreen(onProductClick)
    }
}