package com.example.untitled.luaAdapter.event

import com.example.untitled.api.event.BuiltinEvents
import com.example.untitled.api.player.Player
import com.example.untitled.luaAdapter.entity.SelectableEntityImplLua
import com.example.untitled.luaAdapter.player.PlayerImplBaseLua
import com.example.untitled.luaAdapter.util.BaseLuaTable
import org.luaj.vm2.LuaTable

/**
 * @custom.LuaDoc ---@class OnArrowHitEntityEvent
 * @custom.LuaDoc ---@field arrowUUID string # UUID of the arrow that hit an entity
 * @custom.LuaDoc ---@field victim selectable_entity|player # The entity or player that was hit by the arrow
 */
class OnArrowHitEntityEventLuaClass :
    BaseLuaTable<OnArrowHitEntityEventLuaClass.Container>("OnArrowHitEntityEvent", true) {

    override fun modifyTable(
        table: LuaTable,
        container: Container
    ) {
        val event = container.event
        table.set("arrowUUID", event.arrowUUID.toString())
        val ent = when (event.victim) {
            is Player -> PlayerImplBaseLua().getNewTable(PlayerImplBaseLua.Container(event.victim))
            else -> SelectableEntityImplLua().getNewTable(SelectableEntityImplLua.Container(event.victim))
        }
        table.set("victim", ent)
    }

    override fun checkParseTable(table: LuaTable): Boolean {
        TODO("Not yet implemented")
    }

    override fun fromTable(table: LuaTable): Container? {
        TODO("Not yet implemented")
    }

    data class Container(val event: BuiltinEvents.OnArrowHitEntity) {}
}