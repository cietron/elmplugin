package com.example.untitled.events

import com.destroystokyo.paper.event.server.ServerTickEndEvent
import com.example.untitled.Untitled
import com.example.untitled.luaLoader.EventManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.luaj.vm2.LuaValue

class onTick : Listener {

    @EventHandler
    fun onTick(event: ServerTickEndEvent) {
        Untitled.eventManager.dispatch(EventManager.EventType.onTick, LuaValue.NIL)
        Untitled.newEventManager.emit(Untitled.newEventManager.factory.createOnTick())
    }
}
