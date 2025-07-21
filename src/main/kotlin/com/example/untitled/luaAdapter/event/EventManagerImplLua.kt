package com.example.untitled.luaAdapter.event

import com.example.untitled.api.event.Event
import com.example.untitled.api.event.EventListener
import com.example.untitled.api.event.EventManager
import com.example.untitled.luaAdapter.event.DynamicLuaEventFactory.Companion.eventToLuaValue
import com.example.untitled.luaAdapter.util.BaseLuaTable
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

// lua scripts can listen to builtin and user-created events,
// but can only emit user-created events
class EventManagerImplLua(private val impl: EventManager) : BaseLuaTable<Unit>("event_manager", true) {

    override fun modifyTable(table: LuaTable, container: Unit) {
        table.set("emit", this.emitEventFunc)
        table.set("registerListener", this.registerEventFunc)
    }

    override fun checkParseTable(table: LuaTable): Boolean {
        // not used
        return false
    }

    override fun fromTable(table: LuaTable) {
        // not used
    }

    val emitEventFunc = object : TwoArgFunction() {
        override fun call(arg1: LuaValue?, arg2: LuaValue?): LuaValue? {
            if (arg1 == null) {
                return LuaValue.error("Event type is required")
            }

            if (!arg1.isstring()) {
                return LuaValue.error("Event type must be a string")
            }

            val eventTypeName = arg1.tojstring()

            val eventData = when {
                arg2 == null || arg2.isnil() -> LuaTable()
                arg2.istable() -> arg2 as LuaTable
                else -> return LuaValue.error("Event data must be a table or nil")
            }

            try {
                val event = LuaUserEvent(eventTypeName, eventData)
                impl.emit(event)
                return LuaValue.TRUE
            } catch (e: Exception) {
                return LuaValue.error("Failed to emit event: ${e.message}")
            }
        }
    }

    val registerEventFunc = object : TwoArgFunction() {
        override fun call(eventType: LuaValue?, listener: LuaValue?): LuaValue {
            if (eventType == null || listener == null) {
                return error("Both eventType and listener are required")
            }

            if (!eventType.isstring()) {
                return error("eventType must be a string")
            }

            if (!listener.isfunction()) {
                return error("listener must be a function")
            }

            val eventTypeName = eventType.tojstring()!!
            val luaFunction = listener as LuaFunction

            val kotlinListener = object : EventListener<Event> {
                override fun call(parameter: Event): Boolean {

                    if (parameter is LuaUserEvent && parameter.name != eventTypeName) {
                        return true
                    }

                    try {
                        val luaEvent = eventToLuaValue(parameter)
                        val result = luaFunction.call(luaEvent)

                        if (result.isboolean()) {
                            return result.toboolean()
                        }

                        return false
                    } catch (e: Exception) {
                        return false
                    }
                }
            }

            try {
                impl.registerEvent(
                    DynamicLuaEventFactory().getEventClass(eventTypeName),
                    kotlinListener
                )
                return LuaValue.TRUE
            } catch (e: Exception) {
                return error("Failed to register event listener: ${e.message}")
            }
        }
    }


}