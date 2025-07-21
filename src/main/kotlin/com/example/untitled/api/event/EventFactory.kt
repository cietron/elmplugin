package com.example.untitled.api.event

interface EventFactory {
    fun createOnTick(): BuiltinEvents.Companion.OnTick
}