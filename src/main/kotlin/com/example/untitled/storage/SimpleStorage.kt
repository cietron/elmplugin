package com.example.untitled.storage

import com.example.untitled.Untitled

class SimpleStorage {
    private val storage: HashMap<String, SafeAttributeValue> = HashMap()

    fun store(key: String, value: Any): Boolean {
        val safeValue =
            when (value) {
                is Int -> SafeAttributeValue.IntValue(value)
                is Double -> SafeAttributeValue.DoubleValue(value)
                is String -> SafeAttributeValue.StringValue(value)
                else -> return false
            }

        this.storage[key] = safeValue
        return true
    }

    fun retrieve(key: String): SafeAttributeValue? {
        return this.storage[key]
    }

    fun deleteKey(key: String) {
        this.storage.remove(key)
    }

    fun debugDump() {
        Untitled.instance.logger.info("=== SimpleStorage Debug Dump ===")
        if (storage.isEmpty()) {
            Untitled.instance.logger.info("Storage is empty.")
        } else {
            for ((key, value) in storage) {
                val valueStr =
                    when (value) {
                        is SafeAttributeValue.IntValue -> "Int(${value.value})"
                        is SafeAttributeValue.DoubleValue -> "Double(${value.value})"
                        is SafeAttributeValue.StringValue -> "String(\"${value.value}\")"
                    }
                Untitled.instance.logger.info("$key -> $valueStr")
            }
        }
        Untitled.instance.logger.info("=== End of Dump ===")
    }
}

sealed class SafeAttributeValue {
    data class IntValue(val value: Int) : SafeAttributeValue()

    data class DoubleValue(val value: Double) : SafeAttributeValue()

    data class StringValue(val value: String) : SafeAttributeValue()
}
