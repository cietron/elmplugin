package com.example.untitled.luaAdapter.item

import com.example.untitled.api.item.EquipmentCheckContext
import com.example.untitled.luaAdapter.attribute.AttributeSetClass
import com.example.untitled.luaAdapter.player.PlayerImplBaseLua
import com.example.untitled.luaAdapter.util.BaseLuaTable
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

/**
 * @custom.LuaDoc ---@class equipmentCheckContext
 * @custom.LuaDoc ---@field player player # The player attempting to equip the item
 * @custom.LuaDoc ---@field newAttributeSet attributeSet # The attribute set after equipping
 * @custom.LuaDoc ---@field currentEquipments item[] # List of currently equipped items
 * @custom.LuaDoc ---@desc Context passed to item checkRequirements for equipment validation.
 * @custom.LuaDoc local equipmentCheckContext = {}
 */
class EquipmentCheckContextClass : BaseLuaTable<EquipmentCheckContext>("EquipmentCheckContext", true) {
    override fun modifyTable(
        table: LuaTable,
        container: EquipmentCheckContext
    ) {
        val equipmentList =
            container.currentEquipments.map { equipment -> ItemClass().getNewTable(equipment) }.toTypedArray()
        val luaEquipmentArray = LuaValue.listOf(equipmentList)
        val luaPlayer = PlayerImplBaseLua().getNewTable(PlayerImplBaseLua.Container(container.player))
        val luaAttributeSet = AttributeSetClass().getNewTable(container.newAttributeSet)

        table.set("player", luaPlayer)
        table.set("newAttributeSet", luaAttributeSet)
        table.set("currentEquipments", luaEquipmentArray)
    }

    override fun checkParseTable(table: LuaTable): Boolean {
        TODO("Not yet implemented")
    }

    override fun fromTable(table: LuaTable): EquipmentCheckContext? {
        TODO("Not yet implemented")
    }
}