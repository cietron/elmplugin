package com.example.untitled.luaAdapter.player

import com.example.untitled.api.message.Message
import com.example.untitled.api.player.Player
import net.kyori.adventure.text.Component
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction

class SendPlainChatMessage(val player: Player) : OneArgFunction() {
    override fun call(arg: LuaValue): LuaValue? {
        if (!arg.isstring()) return error("arg is not string.")

        val plainMsg = arg.tojstring()

        player.sendMessage(object : Message {
            override val type = Message.MessageType.CHAT
            override val identifier = "luaAdapterChatMessage"
            override fun get(): Component {
                return Component.text(plainMsg)
            }

            override fun getScoreboardEntries() = null
        })
        return NIL
    }
}