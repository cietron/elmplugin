package com.example.untitled.luaAdapter.player

import com.example.untitled.api.player.Player
import com.example.untitled.apiImpl.entity.EntityFactory
import com.example.untitled.luaAdapter.entity.selectable.SelectableEntityImplLua
import com.example.untitled.luaAdapter.util.BaseLuaTable
import org.luaj.vm2.LuaTable
import java.util.*

/**
 * @custom.LuaDoc ---@class Player : selectable_entity
 * @custom.LuaDoc ---@field name string The player's display name
 * @custom.LuaDoc ---@field setCooldown fun(spellName: string, tickDuration: integer): boolean Sets a cooldown for a spell
 * @custom.LuaDoc ---@field sendMessage fun(msg: message): nil Sends a message to the player. Throws error if the message table is not valid.
 * @custom.LuaDoc ---@field sendPlainChatMessage fun(plainMessage: string)
 * @custom.LuaDoc ---@field sendActionbarMessage fun(message: string): boolean Sends a message to the player's action bar
 * @custom.LuaDoc ---@field getEquipments fun(): item[] # Partial completed item class. Available fields: identifier, vanillaItemID, type
 * @custom.LuaDoc local Player = {}
 */
class PlayerImplBaseLua : BaseLuaTable<PlayerImplBaseLua.Container>(CLASS_NAME, true) {

    companion object {
        const val CLASS_NAME = "player"
    }

    override fun modifyTable(table: LuaTable, container: Container) {

        val player = container.player

        SelectableEntityImplLua().getTable(table, SelectableEntityImplLua.Container(player))

        table.set("name", player.name)
        table.set("sendMessage", SendMessage(player))
        table.set("sendPlainChatMessage", SendPlainChatMessage(player))
        table.set("setCooldown", SetSpellCooldown(player))
        table.set("sendActionbarMessage", SendActionbarMessage(player))
        table.set("getEquipments", GetEquipments(player))
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
