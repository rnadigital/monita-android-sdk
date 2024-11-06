package com.rnadigital.monita_android_sdk.sendData

data class RequestPayload(
    val t: String,             // User-provided token
    val dm: String,            // Deployment method (e.g., "app")
    val mv: String,            // SDK version
    val sv: String,            // App version (you can set this dynamically)
    val tm: Double,            // Unix time in seconds with milliseconds
    val e: String,             // Vendor event
    val vn: String,            // Vendor name (case-sensitive)
    val st: String?,           // Tag status (success or failed), can be null
    val m: String,             // HTTP method (e.g., POST)
    val vu: String,            // Captured HTTP call endpoint URL
    val u: String,             // App ID
    val p: String?,            // App area or null
    var dt: List<Map<String, Any>>,  // Payload content as dynamic JSON array
//    val dt: String,  // Payload content as dynamic JSON array
    val s: String = "android-sdk",  // System (android-sdk by default)
    val rl: String,            // SDK release version
    val env: String = "production", // Default environment (production)
    val `do`: String,        // Host app version
    val et: Int = 0,           // Execution time in seconds (or 0)
    val vid: String = "1",     // Hard-coded "1"
    val cn: String,            // Consent string value
    val sid: String,           // Session ID (can be SDK generated or provided)
    val cid: String?           // Customer ID (null or SDK generated)
)



