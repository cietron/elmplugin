package com.example.untitled.apiImpl.entity

import com.example.untitled.Untitled
import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.storage.SafeAttributeValue
import java.util.*

open class SelectableEntityImpl(override val uuid: UUID) : SelectableEntity {
    companion object {
        const val DEFAULT_HEALTH = 20
        const val DEFAULT_MANA = 100
    }

    override fun getAttribute(attributeName: String): SafeAttributeValue? {
        val key = "$uuid#$attributeName"
        return Untitled.simpleStorage.retrieve(key)
    }

    override fun setAttribute(attributeName: String, value: Any): Boolean {
        val key = "$uuid#$attributeName"
        return Untitled.simpleStorage.store(key, value)
    }

    override var health: Int
        get() {
            val key = "$uuid#health"

            if (Untitled.simpleStorage.retrieve(key) == null) {
                Untitled.simpleStorage.store(key, DEFAULT_HEALTH)
            }

            val value = Untitled.simpleStorage.retrieve(key)
            return when (value) {
                is SafeAttributeValue.DoubleValue -> null
                is SafeAttributeValue.IntValue -> value.value
                is SafeAttributeValue.StringValue -> null
                null -> null
            }!!
        }
        set(value) {
            val key = "$uuid#health"
            Untitled.simpleStorage.store(key, value)
        }

    override var mana: Int
        get() {
            val key = "$uuid#mana"

            if (Untitled.simpleStorage.retrieve(key) == null) {
                Untitled.simpleStorage.store(key, DEFAULT_MANA)
            }

            val value = Untitled.simpleStorage.retrieve(key)

            return when (value) {
                is SafeAttributeValue.DoubleValue -> null
                is SafeAttributeValue.IntValue -> value.value
                is SafeAttributeValue.StringValue -> null
                null -> null
            }!!
        }
        set(value) {
            val key = "$uuid#mana"
            Untitled.simpleStorage.store(key, value)
        }
}
