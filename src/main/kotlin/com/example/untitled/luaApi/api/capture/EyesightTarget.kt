package com.example.untitled.luaApi.api.capture

import org.luaj.vm2.LuaString

interface EyesightTarget {
    // returns nil or entity uuid
    fun get(casterUUID: LuaString): LuaString?
}
