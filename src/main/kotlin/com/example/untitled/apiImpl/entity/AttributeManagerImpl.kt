package com.example.untitled.apiImpl.entity

import com.example.untitled.Untitled
import com.example.untitled.api.attribute.AttributeManager
import com.example.untitled.storage.Storage
import com.example.untitled.storage.StorageValue
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import java.util.*


class AttributeManagerImpl(val storage: Storage) : AttributeManager {
    val defaultValues = HashMap<String, Double>()

    override fun registerKey(key: String, defaultValue: Double) {
        this.defaultValues.put(key, defaultValue)
    }

    override fun clean() {
        Bukkit.getWorlds().forEach { world ->
            world.entities.forEach { entity ->
                if (entity is LivingEntity) {
                    for (key in Registry.ATTRIBUTE) {
                        entity.getAttribute(key)?.removeModifier(getNamespace())
                    }
                }
            }
        }
    }

    override fun get(entity: LivingEntity): List<Map.Entry<String, Double>> {
        val result = mutableListOf<Map.Entry<String, Double>>()

        Registry.ATTRIBUTE.forEach { attribute ->
            val vv = this.getVanillaAttributeByKey(entity, attribute.key)
            if (vv != null) {
                result.add(vv)
            }
        }

        for (key in this.defaultValues.keys) {

            val value = storage.retrieve(this.getStorageKey(entity.uniqueId, key))
            val doubleValue = when (value) {
                is StorageValue.IntValue -> value.value.toDouble()
                is StorageValue.DoubleValue -> value.value
                is StorageValue.StringValue -> Double.NaN
                null -> this.defaultValues[key]
            }

            if (doubleValue == Double.NaN) {
                Untitled.instance.logger.warning("Attribute key $key incorrect storage type. Found $doubleValue.")
            }

            if (doubleValue is Double) {
                result.add(AbstractMap.SimpleEntry(key, doubleValue))
            }
        }

        return result.toList()
    }

    override fun notifyChange(entity: LivingEntity, key: String, newValue: Double) {
        if (this.changeVanillaAttribute(entity, key, newValue)) {
            return
        }

        storage.store(this.getStorageKey(entity.uniqueId, key), StorageValue.DoubleValue(newValue))
    }

    private fun changeVanillaAttribute(entity: LivingEntity, name: String, value: Double): Boolean {
        try {
            val attributeKey = Registry.ATTRIBUTE.getOrThrow(NamespacedKey.minecraft(name))
            val attribute = entity.getAttribute(attributeKey)
            attribute ?: return false
            val diff = value - attribute.baseValue
            // existing modifier will be overwritten
            val modifier = AttributeModifier(getNamespace(), diff, AttributeModifier.Operation.ADD_NUMBER)
            attribute.removeModifier(getNamespace())
            attribute.addModifier(modifier)

        } catch (e: NoSuchElementException) {
            return false
        }

        return true
    }

    private fun getVanillaAttribute(entity: LivingEntity, name: String): Map.Entry<String, Double>? {
        return this.getVanillaAttributeByKey(entity, NamespacedKey.minecraft(name))
    }

    private fun getVanillaAttributeByKey(entity: LivingEntity, key: NamespacedKey): Map.Entry<String, Double>? {
        try {
            val attr = Registry.ATTRIBUTE.getOrThrow(key)
            val value = entity.getAttribute(attr)?.value ?: return null
            return AbstractMap.SimpleEntry(key.key, value)
        } catch (e: NoSuchElementException) {
            return null
        }
    }

    private fun getStorageKey(entityUUID: UUID, key: String): String {
        return "AttributeManager#Attribute#$entityUUID#$key"
    }

    private fun getNamespace(): NamespacedKey {
        return NamespacedKey.fromString("attribute_manager", Untitled.instance)!!
    }
}