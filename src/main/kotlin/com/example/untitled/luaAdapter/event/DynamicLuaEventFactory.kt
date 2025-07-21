package com.example.untitled.luaAdapter.event

import com.example.untitled.api.event.BuiltinEvents
import com.example.untitled.api.event.Event
import com.example.untitled.api.event.EventFactory
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import kotlin.reflect.KClass

interface LuaEventFactory {
    fun createEvent(type: String, data: LuaTable): Event
    fun getEventClass(type: String): KClass<out Event>
}

class DynamicLuaEventFactory(impl: EventFactory) : LuaEventFactory {
    private val eventClasses = HashMap<String, KClass<out Event>>()

    init {
        eventClasses["on_tick"] = impl.createOnTick()::class
    }

    companion object {
        fun eventToLuaValue(event: Event): LuaValue {
            return when (event) {
                is BuiltinEvents.Companion.OnTick -> LuaValue.NIL
                is LuaUserEvent -> event.data
                else -> LuaValue.NIL
            }
        }
    }

    override fun createEvent(type: String, data: LuaTable): Event {
        // Currently, user lua script cannot emit builtin events
        return when (type) {
            else -> LuaUserEvent(type, data)
        }
    }

    override fun getEventClass(type: String): KClass<out Event> {
        return eventClasses.getOrPut(type) { LuaUserEvent::class }
    }


}
