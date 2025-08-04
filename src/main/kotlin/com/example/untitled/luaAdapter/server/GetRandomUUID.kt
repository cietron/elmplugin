package com.example.untitled.luaAdapter.server

import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ZeroArgFunction
import java.util.*

class GetRandomUUID : ZeroArgFunction() {
    override fun call(): LuaValue? {
        return valueOf(UUID.randomUUID().toString())
    }
}