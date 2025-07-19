package com.example.untitled.luaApi.api.events

import org.luaj.vm2.LuaBoolean

interface onTickEvent {
    fun onEvent(): LuaBoolean
}
