package com.example.untitled.api.spell

import net.kyori.adventure.text.Component

interface Spell {
    val description: Component

    fun preCheck(): Boolean
    fun execute()
}