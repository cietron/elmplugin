package com.example.untitled.luaApi.api.entity

import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable

interface EntityLocation {
    fun getLocation(entityUUID: LuaString): LuaTable
}
