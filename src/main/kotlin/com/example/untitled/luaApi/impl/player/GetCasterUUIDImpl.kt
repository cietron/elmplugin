package com.example.untitled.luaApi.impl.player

import com.example.untitled.luaApi.api.player.getCasterUUID
import java.util.UUID
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ZeroArgFunction

class GetCasterUUIDImpl(val casterUUID: UUID) : getCasterUUID, ZeroArgFunction() {
    override fun call_b(): LuaString {
        return valueOf(casterUUID.toString())
    }

    override fun call(): LuaValue? {
        return this.call_b()
    }
}
