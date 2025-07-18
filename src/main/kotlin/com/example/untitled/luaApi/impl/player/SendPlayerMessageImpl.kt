package com.example.untitled.luaApi.impl.player

import com.example.untitled.luaApi.api.player.SendPlayerMessage
import org.bukkit.Bukkit
import org.luaj.vm2.LuaBoolean
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class SendPlayerMessageImpl : SendPlayerMessage, TwoArgFunction() {
    override fun call_b(playerName: LuaString, message: LuaString): LuaBoolean {
        if (
            !playerName.isstring() || !message.isstring()
        ) {
            return FALSE
        }
        val player = Bukkit.getServer().getPlayer(playerName.toString())
        if (player == null) {
            println("Failed to find player $player")
            return FALSE
        }
        player.sendMessage(message.toString())
        //            println("Sent message: ${message.tostring()} to player:
        // ${playername.tostring()}");
        return TRUE
    }

    override fun call(arg1: LuaValue?, arg2: LuaValue?): LuaValue? {
        if (arg1 == null || arg2 == null || !arg1.isstring() || !arg2.isstring()) {
            return FALSE
        }
        return this.call_b(arg1 as LuaString, arg2 as LuaString)
    }
}
