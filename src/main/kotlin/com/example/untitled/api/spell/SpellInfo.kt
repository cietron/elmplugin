package com.example.untitled.api.spell

import java.util.function.Supplier

interface SpellInfo {
    val cooldown: Int
    val description: String
    val requirements: Supplier<Boolean>
    val body: Runnable
}