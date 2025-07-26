package com.example.untitled.luaAdapter.event

import com.example.untitled.api.event.BuiltinEvents
import com.example.untitled.luaAdapter.entity.LuaEntityFactory
import com.example.untitled.luaAdapter.util.BaseLuaTable
import org.luaj.vm2.LuaTable

/**
 * @custom.LuaDoc ---@class OnFireballHitEntityEvent
 * @custom.LuaDoc ---@field fireballUUID string # UUID of the fireball that hit an entity
 * @custom.LuaDoc ---@field victim selectable_entity|player # The entity or player that was hit by the fireball
 */
class OnFireballHitEntityLuaClass :
    BaseLuaTable<OnFireballHitEntityLuaClass.Container>("OnFireballHitEntityEvent", true) {

    override fun modifyTable(
        table: LuaTable,
        container: Container
    ) {
        val event = container.event
        table.set("fireballUUID", event.fireballUUID.toString())
        table.set("victim", LuaEntityFactory.getLuaValue(event.victim))
    }

    override fun checkParseTable(table: LuaTable): Boolean {
        TODO("Not yet implemented")
    }

    override fun fromTable(table: LuaTable): Container? {
        TODO("Not yet implemented")
    }

    data class Container(val event: BuiltinEvents.OnFireballHitEntity)
}