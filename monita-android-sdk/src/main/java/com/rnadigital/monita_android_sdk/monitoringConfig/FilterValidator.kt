package com.rnadigital.monita_android_sdk.monitoringConfig

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.rnadigital.monita_android_sdk.Logger

object FilterValidator {

    fun excludeParameters(
        dataList: List<Map<String, Any>>,
        excludeParameters: List<String> = emptyList()
    ): List<Map<String, Any>> {
        return dataList.map { data ->
            val filteredData = mutableMapOf<String, Any>()

            data.forEach { (key, value) ->
                if (key !in excludeParameters) {
                    when (value) {
                        is Map<*, *> -> {
                            // Recursively exclude parameters in nested maps
                            @Suppress("UNCHECKED_CAST")
                            filteredData[key] = excludeParameters(
                                listOf(value as Map<String, Any>),
                                excludeParameters
                            ).first()
                        }

                        is List<*> -> {
                            // Process lists by filtering each item if it’s a map
                            filteredData[key] = value.map { item ->
                                if (item is Map<*, *>) {
                                    excludeParameters(
                                        listOf(item as Map<String, Any>),
                                        excludeParameters
                                    ).first()
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

            Logger().log("Intercepted validateFilters started ")

            var filterMatchFound = false
            for (data in dtData) {

                val value = findValueByKey(data, key) // Use recursive key search
                Logger().log("Intercepted FilterValidator key $key value $value ")

                if (value == null) {
                    if (op == "exist") {
                        Logger().log("Intercepted FilterValidator exist Key must exist ")
                        return false // Key must exist
                    } else if (op == "not_exist") {
                        filterMatchFound = true
                        Logger().log("FilterValidator not_exist Key should not exist ")
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


    fun convertListToJson(list: Map<String, Any>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    fun fixInvalidJsonString(input: String): String {
        return input
            .replace("=", ":") // Replace '=' with ':'
            .replace("([a-zA-Z0-9_]+)\\s*:".toRegex(), "\"$1\":") // Add quotes around keys
            .replace("'".toRegex(), "\"") // Replace single quotes with double quotes if needed
    }


    fun getValueFromJsonPath(list: Map<String, Any>, jsonPath: String): Any? {

        val jsonString = convertListToJson(list).trimIndent()
        Logger().log("Intercepted jsonString $jsonString ")


        val gson = Gson()
        val jsonElement: JsonElement = gson.fromJson(jsonString, JsonElement::class.java)

        // Split the jsonPath into parts (e.g., "commerce.items[0].itemNumber")
        val pathParts = jsonPath.split(".")
        var currentElement: JsonElement? = jsonElement

        for (part in pathParts) {
            if (currentElement is JsonObject) {
                // Handle array indexing if present
                if (part.contains("[")) {
                    val arrayPart = part.substringBefore("[")
                    val index = part.substringAfter("[").substringBefore("]").toInt()
                    currentElement =
                        currentElement.getAsJsonObject(arrayPart)?.asJsonArray?.get(index)
                } else {
                    currentElement = currentElement.getAsJsonObject()?.get(part)
                }
            }
        }

        return currentElement?.let {
            if (it.isJsonPrimitive) it.asJsonPrimitive.asString else it.toString()
        }
    }


    fun getValueFromJson(list: Map<String, Any>, jsonPath: String): JsonElement? {
        // Parse the JSON string into a JsonObject
        val jsonString = convertListToJson(list).trimIndent()
        Logger().log("Intercepted jsonString $jsonString ")

        val jsonElement: JsonElement = JsonParser.parseString(jsonString)
        if (jsonElement !is JsonObject) return null

        // Construct the path segments
        val pathSegments = constructJsonPath(jsonPath)

        // Traverse the JSON object using the path segments
        var currentElement: JsonElement = jsonElement
        for (segment in pathSegments) {
            currentElement = when (segment) {
                is String -> (currentElement as? JsonObject)?.get(segment) ?: return null
                is Int -> (currentElement as? com.google.gson.JsonArray)?.get(segment) ?: return null
                else -> return null
            }
        }

        return currentElement
    }

    fun constructJsonPath(jsonPath: String): List<Any> {
        val pathSegments = mutableListOf<Any>()
        val regex = Regex("([a-zA-Z0-9_]+)|\\[(\\d+)]")

        regex.findAll(jsonPath).forEach { matchResult ->
            val key = matchResult.groups[1]?.value
            val index = matchResult.groups[2]?.value

            when {
                key != null -> pathSegments.add(key)  // Add the key to the path
                index != null -> pathSegments.add(index.toInt())  // Convert index to integer and add
            }
        }

        return pathSegments
    }


    private fun evaluateCondition(value: Any?, op: String, values: List<String>): Boolean {
        Logger().log("Intercepted FilterValidator evaluateCondition ")

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
