package com.example.untitled.luaApi.api.entity

import org.luaj.vm2.LuaNumber
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

interface CubicBezierMotion {
    fun move(
        startPoint: LuaTable,
        endPoint: LuaTable,
        bezierCurve: LuaTable,
        duration: LuaNumber,
        entityUUID: LuaString,
    ): LuaValue
}
