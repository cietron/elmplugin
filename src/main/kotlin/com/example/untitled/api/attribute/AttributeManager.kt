package com.example.untitled.api.attribute

import org.bukkit.entity.LivingEntity

interface AttributeManager {


    fun registerKey(key: String, defaultValue: Double)

    fun get(entity: LivingEntity): List<Map.Entry<String, Double>>

    fun notifyChange(entity: LivingEntity, key: String, newValue: Double)

    fun clean()
}