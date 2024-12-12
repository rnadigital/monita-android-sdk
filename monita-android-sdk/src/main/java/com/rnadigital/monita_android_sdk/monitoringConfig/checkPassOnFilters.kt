package com.rnadigital.monita_android_sdk.monitoringConfig

import com.rnadigital.monita_android_sdk.monitoringConfig.FilterValidator.findValueByKey

fun checkPassOnFilters(data: List<Map<String, Any>>, filters: List<Filter>, joinOperator: String): Boolean {
    val exitImmediately = joinOperator == "AND"
    val results = mutableListOf<Boolean>()

    for (filter in filters) {
        var pass = true
        val filterValues = filter.`val` ?: listOf()
        val value = findValueByKey(data, filter.key) // Fetch the value from the nested data

        when (filter.op) {
            "eq", "contains" -> {
                val checkFn: (Any?, String) -> Boolean = when (filter.op) {
                    "eq" -> { value, filterVal -> value?.toString() == filterVal }
                    "contains" -> { value, filterVal -> value is String && value.contains(filterVal) }
                    else -> { _, _ -> false } // Fallback for unexpected cases
                }

                pass = filterValues.any { checkFn(value, it) }
                if (!pass && exitImmediately) return false
            }

            "ne" -> {
                pass = filterValues.none { it == value?.toString() }
                if (!pass && exitImmediately) return false
            }

            "blank" -> {
                pass = value == null || (value is String && value.isEmpty())
                if (!pass && exitImmediately) return false
            }

            "not_blank" -> {
                pass = value != null && (value !is String || value.isNotEmpty())
                if (!pass && exitImmediately) return false
            }

            "exist" -> {
                pass = value != null
                if (!pass && exitImmediately) return false
            }

            "not_exist" -> {
                pass = value == null
                if (!pass && exitImmediately) return false
            }
        }

        results.add(pass)
    }

    return if (exitImmediately) {
        results.all { it }
    } else {
        filters.isEmpty() || results.any { it }
    }
}

fun findValueByKey(data: List<Map<String, Any>>, key: String): Any? {
    val keys = key.split(".")
    for (item in data) {
        var current: Any? = item
        for (k in keys) {
            if (current is Map<*, *> && current.containsKey(k)) {
                current = current[k]
            } else {
                current = null
                break
            }
        }
        if (current != null) return current // Return the first matching value
    }
    return null
}
