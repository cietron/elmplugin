package com.example.untitled.luaApi.api.entity

import org.luaj.vm2.LuaString

interface KillEntity {
    fun kill(entityUUID: LuaString)
}
