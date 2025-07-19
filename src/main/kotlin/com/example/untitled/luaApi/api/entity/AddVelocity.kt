package com.example.untitled.luaApi.api.entity

import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable

interface AddVelocity {
    fun add(entityUUID: LuaString, vec: LuaTable)
}
