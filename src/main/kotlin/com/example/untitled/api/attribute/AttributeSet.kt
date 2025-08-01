package com.example.untitled.api.attribute

import org.jetbrains.annotations.Contract

class AttributeSet private constructor(private val attributes: Map<String, Attribute>) {
    operator fun get(key: String): Double = attributes[key]?.value ?: 0.0

    @Contract(pure = true)
    operator fun plus(other: AttributeSet): AttributeSet = merge(this, other)
    operator fun plus(attribute: Attribute): AttributeSet = this + AttributeSet.of(attribute)

    fun keys(): Set<String> = attributes.keys
    fun values(): Collection<Attribute> = attributes.values
    fun copy(): AttributeSet =
        AttributeSet(attributes.toMap()) // Shallow copy works because attribute is an immutable data class.

    companion object {
        fun of(vararg attributes: Attribute) = AttributeSet(attributes.associateBy { it.identifier })
        fun fromList(attributeList: List<Attribute>) = AttributeSet(attributeList.associateBy { it.identifier })
        fun empty() = AttributeSet(emptyMap())

        @Contract(pure = true)
        private fun merge(a: AttributeSet, b: AttributeSet): AttributeSet {
            val merged = (a.keys() + b.keys()).associateWith { key ->
                Attribute(key, a[key] + b[key])
            }
            return AttributeSet(merged)
        }
    }
}