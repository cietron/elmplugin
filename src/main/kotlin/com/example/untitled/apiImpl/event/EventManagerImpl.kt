package com.example.untitled.apiImpl.event

import com.example.untitled.api.event.Event
import com.example.untitled.api.event.EventListener
import com.example.untitled.api.event.EventManager
import kotlin.reflect.KClass

class EventManagerImpl : EventManager {

    private val listeners = HashMap<KClass<out Event>, MutableList<EventListener<out Event>>>()
    val factory = EventFactoryImpl()

    override fun <T : Event> registerEvent(event: KClass<out T>, listener: EventListener<T>) {
        listeners.getOrPut(event) { ArrayList() }.add(listener)
    }

    override fun <T : Event> emit(event: T) {
        val eventClass = event::class
        val toRemove = ArrayList<EventListener<out Event>>()
        listeners[eventClass]?.forEach { listener ->
            //  This cast is safe because we only store EventListener<T> under key KClass<T>
            //  during registration, so any listener retrieved here was originally EventListener<T>
            @Suppress("UNCHECKED_CAST")
            val res = (listener as EventListener<T>).call(event)
            if (!res) {
                toRemove.add(listener)
            }
        }

        listeners[eventClass]?.removeAll(toRemove)
    }

    fun debugDump() {
        println("===== EventManager Debug Dump =====")
        listeners.forEach { (eventClass, eventListeners) ->
            println("Event: ${eventClass.simpleName}, Listeners: ${eventListeners.size}")
            eventListeners.forEach { listener ->
                println("  Listener: ${listener::class.simpleName}")
            }
        }
        println("====================================")
    }
}