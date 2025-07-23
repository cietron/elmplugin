package com.example.untitled.luaAdapter.player

import com.example.untitled.api.player.Player
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction

class SendActionbarMessage(val player: Player) : OneArgFunction() {
    override fun call(msg: LuaValue): LuaValue? {
        if (!msg.isstring()) {
            return error("Expected a string message")
        }

        val message = msg.tojstring()
        player.sendActionBarString(message)
        return LuaValue.TRUE
    }
}