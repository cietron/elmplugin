package com.example.untitled.api.entity

import java.util.*

interface SelectableEntity {
    val uuid: UUID

    fun getAttribute(attributeName: String): Any?

    fun setAttribute(attributeName: String, value: Any): Boolean

    var health: Int
    var mana: Int
}
