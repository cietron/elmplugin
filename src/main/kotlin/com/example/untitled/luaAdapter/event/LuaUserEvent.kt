package com.example.untitled.luaAdapter.event

import com.example.untitled.api.event.Event
import org.luaj.vm2.LuaTable

class LuaUserEvent(val name: String, val data: LuaTable) : Event {
}