package com.rnadigital.monita_android_sdk.monitoringConfig

data class Vendor(
    val eventParamter: String,
    val execludeParameters: List<String>,
    val filters: List<Filter>,
    val urlPatternMatches: List<String>,
    val vendorName: String,
    val filtersJoinOperator: String?

)