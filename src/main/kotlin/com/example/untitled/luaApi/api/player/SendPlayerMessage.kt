package com.example.untitled.luaApi.api.player

import org.luaj.vm2.LuaBoolean
import org.luaj.vm2.LuaString

interface SendPlayerMessage {
    fun call_b(playerName: LuaString, message: LuaString): LuaBoolean
}
