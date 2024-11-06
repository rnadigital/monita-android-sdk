package com.rnadigital.monita_android_sdk.monitoringConfig

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.rnadigital.monita_android_sdk.Logger
import com.google.gson.reflect.TypeToken

object FilterValidator {




    fun modifyJsonAndReturnList(data: List<Map<String, Any>>, targetKeys: List<String>): List<Map<String, Any>> {
        val gson = Gson()

        // Convert List<Map<String, Any>> to JSON string
        val jsonString = gson.toJson(data)

        // Convert JSON string to JsonElement
        val jsonElement: JsonElement = JsonParser.parseString(jsonString)
        if (jsonElement !is com.google.gson.JsonArray) return data // Return original data if JSON conversion fails

        // Call removeKeyFromJson to remove each target key from JSON
        targetKeys.forEach { key ->
            jsonElement.forEach { element ->
                removeKeyFromJson(element, key)
            }
        }

        // Convert modified JsonElement back to List<Map<String, Any>>
        val modifiedJsonString = gson.toJson(jsonElement)
        val type = object : TypeToken<List<Map<String, Any>>>() {}.type
        return gson.fromJson(modifiedJsonString, type)
    }

    // Recursive function to remove a target key from a JsonElement
    fun removeKeyFromJson(jsonElement: JsonElement, targetKey: String) {
        when (jsonElement) {
            is JsonObject -> {
                jsonElement.entrySet().forEach { (key, value) ->
                    if (key == targetKey) {
                        jsonElement.remove(key) // Remove the key if it matches the target key
                    } else {
                        removeKeyFromJson(value, targetKey) // Recursively check nested elements
                    }
                }
            }
            is com.google.gson.JsonArray -> {
                jsonElement.forEach { item ->
                    removeKeyFromJson(item, targetKey)
                }
            }
        }
    }

    fun removeKeyByValue(data: MutableMap<String, Any?>, targetKey: String): MutableMap<String, Any?> {
        // Iterate over the entries of the map
        val iterator = data.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val key = entry.key
            val value = entry.value

            if (key == targetKey) {
                iterator.remove() // Remove the entry if the key matches the target key
            } else if (value is MutableMap<*, *>) {
                @Suppress("UNCHECKED_CAST")
                // Recursively remove the target key in nested maps
                removeKeyByValue(value as MutableMap<String, Any?>, targetKey)
            } else if (value is MutableList<*>) {
                // Iterate over the list if it contains nested maps
                value.forEach { item ->
                    if (item is MutableMap<*, *>) {
                        @Suppress("UNCHECKED_CAST")
                        removeKeyByValue(item as MutableMap<String, Any?>, targetKey)
                    }
                }
            }
        }
        return data
    }



    fun removeKeyFromListOfMaps(
        list: List<Map<String, Any>>,
        keyToRemove: String
    ): List<Map<String, Any>> {
        return list.map { map ->
            map.mapValues { (_, value) ->
                if (value is Map<*, *>) {
                    // Recursively remove the key in nested maps
                    removeKeyFromMap(value as Map<String, Any>, keyToRemove)
                } else if (value is List<*>) {
                    // Recursively apply the function for nested lists
                    removeKeyFromListOfMaps(value.filterIsInstance<Map<String, Any>>(), keyToRemove)
                } else {
                    value
                }
            }
        }
    }

    fun removeKeyFromMap(map: Map<String, Any>, keyToRemove: String): Map<String, Any> {
        return map.filterKeys { it != keyToRemove }.mapValues { (_, value) ->
            when (value) {
                is Map<*, *> -> removeKeyFromMap(value as Map<String, Any>, keyToRemove)
                is List<*> -> removeKeyFromListOfMaps(value.filterIsInstance<Map<String, Any>>(), keyToRemove)
                else -> value
            }
        }
    }


    fun removeKeyFromJson(jsonString: String, keyToRemove: String): JsonElement {
        val jsonElement = JsonParser.parseString(jsonString)
        removeKeyRecursively(jsonElement, keyToRemove)
        return jsonElement
    }

    fun removeKeyRecursively(jsonElement: JsonElement, keyToRemove: String) {
        if (jsonElement.isJsonObject) {
            val jsonObject = jsonElement.asJsonObject
            jsonObject.remove(keyToRemove) // Remove the key if present

            // Recursively check each value in the JsonObject
            jsonObject.entrySet().forEach { (_, value) ->
                removeKeyRecursively(value, keyToRemove)
            }
        } else if (jsonElement.isJsonArray) {
            // For arrays, recursively check each element
            jsonElement.asJsonArray.forEach { element ->
                removeKeyRecursively(element, keyToRemove)
            }
        }
    }


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


    fun getValueFromJson(json: String, jsonPath: String): JsonElement? {
        // Parse the JSON string into a JsonObject
        val jsonString = json.trimIndent()
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
