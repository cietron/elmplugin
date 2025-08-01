package com.example.untitled.api.attribute

import org.bukkit.entity.LivingEntity

interface AttributeManager {

    /**
     * @return a copy of entity's attributeSet
     */
    fun get(entity: LivingEntity): AttributeSet

    fun getDefault(entity: LivingEntity): AttributeSet

    fun notifyChange(entity: LivingEntity, key: String, newValue: Double)

    fun clean()
}