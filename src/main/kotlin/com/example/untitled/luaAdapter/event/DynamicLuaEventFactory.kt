package com.example.untitled.luaAdapter.event

import com.example.untitled.api.event.BuiltinEvents
import com.example.untitled.api.event.Event
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import kotlin.reflect.KClass

interface LuaEventFactory {
    fun createEvent(type: String, data: LuaTable): Event
    fun getEventClass(type: String): KClass<out Event>
}

class DynamicLuaEventFactory : LuaEventFactory {
    private val eventClasses = HashMap<String, KClass<out Event>>()

    init {
        eventClasses["on_tick"] = BuiltinEvents.OnTick::class
        eventClasses["onArrowHitEntity"] = BuiltinEvents.OnArrowHitEntity::class
        eventClasses["onFireballHitEntity"] = BuiltinEvents.OnFireballHitEntity::class
    }

    companion object {
        fun eventToLuaValue(event: Event): LuaValue {
            return when (event) {
                is BuiltinEvents.OnTick -> LuaValue.NIL
                is BuiltinEvents.OnArrowHitEntity -> OnArrowHitEntityEventLuaClass().getNewTable(
                    OnArrowHitEntityEventLuaClass.Container(event)
                )
                is BuiltinEvents.OnFireballHitEntity -> OnFireballHitEntityLuaClass().getNewTable(
                    OnFireballHitEntityLuaClass.Container(event)
                )
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
