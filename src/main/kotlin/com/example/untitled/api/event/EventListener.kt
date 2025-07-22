package com.example.untitled.api.event

fun interface EventListener<T : Event> {
    // EventListener is recurring by default. Return false to unsubscribe to events.
    fun call(parameter: T): Boolean
}