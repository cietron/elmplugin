package com.example.untitled.luaAdapter.player

import com.example.untitled.api.player.Player
import com.example.untitled.apiImpl.entity.EntityFactory
import com.example.untitled.luaAdapter.entity.SelectableEntityImplLua
import com.example.untitled.luaAdapter.util.BaseLuaTable
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import java.util.*

class PlayerImplBaseLua : BaseLuaTable<PlayerImplBaseLua.Container>(CLASS_NAME, true) {

    companion object {
        const val CLASS_NAME = "player"
    }

    override fun modifyTable(table: LuaTable, container: Container) {

        val player = container.player

        SelectableEntityImplLua().getTable(table, SelectableEntityImplLua.Container(player))

        table.set("name", player.name)
        table.set("sendMessage", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue? {
                if (!arg.isstring()) {
                    error("Not string")
                }

                player.sendMessage(arg.tojstring())
                return TRUE
            }

        })

        table.set("setCooldown", SetSpellCooldown(player))
        table.set("sendActionbarMessage", SendActionbarMessage(player))
    }

    override fun checkParseTable(table: LuaTable): Boolean {
        if (table.get("uuid") == null || !table.get("uuid").isstring()) {
            return false
        }
        val uuidString = table.get("uuid").tojstring()
        try {
            UUID.fromString(uuidString)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return false
        }
        return true
    }

    override fun fromTable(table: LuaTable): Container? {
        val uuid = UUID.fromString(table.get("uuid").tojstring())
        val ent = EntityFactory.fromEntityUUID(uuid)

        return when (ent) {
            is Player -> Container(ent)
            else -> null
        }
    }

    data class Container(val player: Player)
}
