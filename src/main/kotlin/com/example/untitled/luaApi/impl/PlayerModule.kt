package com.example.untitled.luaApi.impl

import com.example.untitled.luaApi.impl.player.GetCasterUUIDImpl
import com.example.untitled.luaApi.impl.player.SendPlayerMessageImpl
import org.bukkit.entity.Player
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class PlayerModule(val caster: Player) : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue): LuaValue {
        val library = tableOf()
        library.set("sendPlayerMessage", SendPlayerMessageImpl())
        library.set("GetCasterUUID", GetCasterUUIDImpl(caster.uniqueId))
        env.set("hostname", library)
        env["package"]["loaded"].set("hostname", library)
        return library
    }
}
