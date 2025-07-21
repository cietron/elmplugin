package com.example.untitled.api.event

import kotlin.reflect.KClass

interface EventManager {
    fun <T : Event> registerEvent(event: KClass<out T>, listener: EventListener<T>)

    fun <T : Event> emit(event: T)
}