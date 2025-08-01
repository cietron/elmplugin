package com.example.untitled.luaAdapter.player

import com.example.untitled.api.player.Player
import com.example.untitled.luaAdapter.message.MessageClass
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction

class SendMessage(val player: Player) : OneArgFunction() {
    override fun call(arg: LuaValue): LuaValue? {
        val msg = MessageClass().fromLuaValue(arg) ?: return error("Failed to parse message")
        player.sendMessage(msg)
        return NIL
    }
}