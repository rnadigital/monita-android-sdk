package com.rnadigital.monita_android_sdk.monitoringConfig

object FilterValidator {
    fun validateFilters(dtData: List<Map<String, Any?>>, filters: List<Filter?>): Boolean {
        for (filter in filters) {
            val key: String = filter?.key ?: ""
            val op: String = filter?.op ?: ""
            val values: List<String>? = filter?.value

            // Iterate over each map in dtData list
            var filterMatchFound = false
            for (data in dtData) {
                if (!data.containsKey(key)) {
                    if (op == "exist") {
                        return false // Key must exist
                    } else if (op == "not_exist") {
                        filterMatchFound = true
                        break // Key should not exist; condition is true for this item
                    }
                } else {
                    val value = data[key]
                    val result = values?.let { evaluateCondition(value, op, it) } ?: false
                    if (result) {
                        filterMatchFound = true
                        break // Filter condition met for this item
                    }
                }
            }

            // If no match was found for this filter, validation fails
            if (!filterMatchFound) {
                return false
            }
        }
        return true // All filters passed
    }

    private fun evaluateCondition(value: Any?, op: String, values: List<String>): Boolean {
        when (op) {
            "eq" -> return values.contains(value.toString())
            "ne" -> return !values.contains(value.toString())
            "contains" -> {
                for (`val` in values) {
                    if (value.toString().contains(`val`)) {
                        return true
                    }
                }
                return false
            }
            "blank" -> return value == null || value.toString().trim().isEmpty()
            "not_blank" -> return value != null && value.toString().trim().isNotEmpty()
            "exist" -> return value != null
            "not_exist" -> return value == null
            else -> throw IllegalArgumentException("Unknown operation: $op")
        }
    }
}
