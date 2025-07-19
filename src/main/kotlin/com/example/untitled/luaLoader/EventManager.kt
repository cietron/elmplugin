package com.example.untitled.luaLoader

import org.luaj.vm2.LuaFunction
import org.luaj.vm2.Varargs

class EventManager {
    private val listeners: MutableMap<EventType, MutableList<LuaFunction>> = HashMap()

    init {
        for (type in EventType.entries) {
            this.listeners[type] = ArrayList()
        }
    }

    fun clear() {
        for (type in EventType.entries) {
            this.listeners[type]!!.clear()
        }
    }

    fun addListener(type: EventType, callback: LuaFunction) {
        this.listeners[type]!!.add(callback)
    }

    fun dispatch(type: EventType, varargs: Varargs) {
        for (callback in this.listeners[type]!!) {
            val status = callback.invoke(varargs).arg(1)
            if (status.toboolean()) {
                this.listeners[type]!!.remove(callback)
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
