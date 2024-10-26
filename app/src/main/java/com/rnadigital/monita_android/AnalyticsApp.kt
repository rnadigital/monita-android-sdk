package com.rnadigital.monita_android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun AnalyticsApp(
    onFirebaseAnalyticsClick: () -> Unit,
    onFacebookAnalyticsClick: () -> Unit,
    onAdobeAnalyticsClick: () -> Unit,
    onGoogleAdsClick: () -> Unit,
    onApiCallClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = onFirebaseAnalyticsClick) {
            Text(text = "Firebase Analytics")
        }
        Button(onClick = onFacebookAnalyticsClick) {
            Text(text = "Facebook Analytics")
        }
        Button(onClick = onAdobeAnalyticsClick) {
            Text(text = "Adobe Analytics")
        }
        Button(onClick = onGoogleAdsClick) {
            Text(text = "Load Google Ads")
        }
        Button(onClick = onApiCallClick) {
            Text(text = "HTTP API Call")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AnalyticsApp(
        onFirebaseAnalyticsClick = {},
        onFacebookAnalyticsClick = {},
        onAdobeAnalyticsClick = {},
        onGoogleAdsClick = {},
        onApiCallClick = {}
    )
}