package com.example.untitled.luaLoader

import org.luaj.vm2.LuaFunction
import org.luaj.vm2.Varargs
import java.util.function.Supplier

class EventManager {
    private val listeners: MutableMap<EventType, MutableList<LuaFunction>> = HashMap()
    private val javaListener: MutableMap<EventType, MutableList<Supplier<Boolean>>> = HashMap()

    init {
        for (type in EventType.entries) {
            this.listeners[type] = ArrayList()
            this.javaListener[type] = ArrayList()
        }
    }

    fun clear() {
        for (type in EventType.entries) {
            this.listeners[type]!!.clear()
            this.javaListener[type]!!.clear()
        }
    }

    fun addListener(type: EventType, callback: LuaFunction) {
        this.listeners[type]!!.add(callback)
    }

    fun addJavaListener(type: EventType, callback: Supplier<Boolean>) {
        this.javaListener[type]!!.add(callback)
    }

    fun dispatch(type: EventType, varargs: Varargs) {
        for (callback in this.listeners[type]!!.toList()) {
            val status = callback.invoke(varargs).arg(1)
            if (status.toboolean()) {
                this.listeners[type]!!.remove(callback)
            }
        }

        for (callback in this.javaListener[type]!!.toList()) {
            val status = callback.get()
            if (status) {
                this.javaListener[type]!!.remove(callback)
            }
        }
    }

    enum class EventType(val identifier: String) {
        onTick("onTick");

        companion object {
            private val byIdentifier = entries.associateBy { it.identifier }

            fun fromIdentifier(id: String): EventType? = byIdentifier[id]
        }
    }
}
