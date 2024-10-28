package com.rnadigital.monita_android_sdk.monitoringConfig

object FilterValidator {

    fun excludeParameters(dataList: List<Map<String, Any>>, excludeParameters: List<String> = emptyList()): List<Map<String, Any>> {
        return dataList.map { data ->
            val filteredData = mutableMapOf<String, Any>()

            data.forEach { (key, value) ->
                if (key !in excludeParameters) {
                    when (value) {
                        is Map<*, *> -> {
                            // Recursively exclude parameters in nested maps
                            @Suppress("UNCHECKED_CAST")
                            filteredData[key] = excludeParameters(listOf(value as Map<String, Any>), excludeParameters).first()
                        }
                        is List<*> -> {
                            // Process lists by filtering each item if it’s a map
                            filteredData[key] = value.map { item ->
                                if (item is Map<*, *>) {
                                    excludeParameters(listOf(item as Map<String, Any>), excludeParameters).first()
                                } else {
                                    item
                                }
                            }
                        }
                        else -> {
                            // Add non-map, non-list values directly
                            filteredData[key] = value
                        }
                    }
                }
            }

            filteredData
        }
    }



    // Recursive function to search for a key within a nested map structure
     fun findValueByKey(data: Map<String, Any?>, targetKey: String): Any? {
        data.forEach { (key, value) ->
            if (key == targetKey) {
                return value // Return the value if key matches
            } else if (value is Map<*, *>) {
                @Suppress("UNCHECKED_CAST")
                val result = findValueByKey(value as Map<String, Any?>, targetKey)
                if (result != null) return result
            } else if (value is List<*>) {
                value.forEach { item ->
                    if (item is Map<*, *>) {
                        @Suppress("UNCHECKED_CAST")
                        val result = findValueByKey(item as Map<String, Any?>, targetKey)
                        if (result != null) return result
                    }
                }
            }
        }
        return null // Return null if key is not found
    }

    fun validateFilters(dtData: List<Map<String, Any?>>, filters: List<Filter?>): Boolean {
        for (filter in filters) {
            val key: String = filter?.key ?: ""
            val op: String = filter?.op ?: ""
            val values: List<String>? = filter?.`val`

            println("Intercepted validateFilters started ")

            var filterMatchFound = false
            for (data in dtData) {

                val value = findValueByKey(data, key) // Use recursive key search
                println("Intercepted FilterValidator key $key value $value ")

                if (value == null) {
                    if (op == "exist") {
                        println("Intercepted FilterValidator exist Key must exist ")
                        return false // Key must exist
                    } else if (op == "not_exist") {
                        filterMatchFound = true
                        println("FilterValidator not_exist Key should not exist ")
                        break // Key should not exist; condition is true for this item
                    }
                } else {
                    val result = values?.let { evaluateCondition(value, op, it) } ?: false
                    if (result) {
                        filterMatchFound = true
                        break // Filter condition met for this item
                    }
                }
            }

            if (!filterMatchFound) {
                return false
            }
        }
        return true // All filters passed
    }

    private fun evaluateCondition(value: Any?, op: String, values: List<String>): Boolean {
        println("Intercepted FilterValidator evaluateCondition ")

        return when (op) {
            "eq" -> values.contains(value.toString())
            "ne" -> !values.contains(value.toString())
            "contains" -> values.any { value.toString().contains(it) }
            "blank" -> value == null || value.toString().trim().isEmpty()
            "not_blank" -> value != null && value.toString().trim().isNotEmpty()
            "exist" -> value != null
            "not_exist" -> value == null
            else -> throw IllegalArgumentException("Unknown operation: $op")
        }
    }
}
