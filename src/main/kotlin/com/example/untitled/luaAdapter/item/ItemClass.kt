package com.example.untitled.luaAdapter.item

import com.example.untitled.api.attribute.AttributeSet
import com.example.untitled.api.item.Equipment
import com.example.untitled.api.item.EquipmentCheckContext
import com.example.untitled.api.item.EquipmentType
import com.example.untitled.luaAdapter.attribute.AttributeSetClass
import com.example.untitled.luaAdapter.util.BaseLuaTable
import net.kyori.adventure.text.Component
import org.luaj.vm2.LuaTable

/**
 * @custom.LuaDoc ---@class item
 * @custom.LuaDoc ---@field identifier string # Unique identifier for the item
 * @custom.LuaDoc ---@field vanillaItemID string # The vanilla Minecraft item ID
 * @custom.LuaDoc ---@field type string # Refer the api enum class.
 * @custom.LuaDoc ---@field displayName string # Display name of the item
 * @custom.LuaDoc ---@field attributeModifiers attributeSet # Attribute modifiers (delta) for the item
 * @custom.LuaDoc ---@field checkRequirements fun(context: equipmentCheckContext): boolean # Function to check if the item can be equipped
 * @custom.LuaDoc ---@desc Implementation note: The attributes modifiers of equipments are aggregated into a single attributeSet
 * @custom.LuaDoc ---@desc Therefore a player might equip equipments that are not possible to equip on a one by one basis (circular dependency).
 * @custom.LuaDoc local item = {}
 */
class ItemClass : BaseLuaTable<Equipment>("items", true) {
    override fun modifyTable(table: LuaTable, container: Equipment) {
        table.set("identifier", container.identifier)
        table.set("vanillaItemID", container.vanillaItemID)
        table.set("type", container.type.toString())
    }

    override fun checkParseTable(table: LuaTable): Boolean {
        val identifier = table.get("identifier")
        val vanillaItemID = table.get("vanillaItemID")
        val type = table.get("type")
        val displayName = table.get("displayName")
        val attributeModifiers = table.get("attributeModifiers")
        val checkRequirementsFunc = table.get("checkRequirements")

        return identifier.isstring() &&
                vanillaItemID.isstring() &&
                type.isstring() &&
                displayName.isstring() &&
                attributeModifiers.istable() &&
                checkRequirementsFunc.isfunction()
    }

    override fun fromTable(table: LuaTable): Equipment? {
        if (!checkParseTable(table)) return null

        val identifier = table.get("identifier").tojstring()
        val vanillaItemID = table.get("vanillaItemID").tojstring()
        val typeStr = table.get("type").tojstring()
        val displayNameStr = table.get("displayName").tojstring()
        val attributeModifiersTable = table.get("attributeModifiers").checktable()
        val checkRequirementsFunc = table.get("checkRequirements").checkfunction()

        val type = try {
            EquipmentType.valueOf(typeStr.uppercase())
        } catch (e: IllegalArgumentException) {
            return null
        }

        val attributeModifiers: AttributeSet = AttributeSetClass().fromLuaValue(attributeModifiersTable) ?: return null

        return object : Equipment {
            override val identifier: String = identifier
            override val vanillaItemID: String = vanillaItemID
            override val type: EquipmentType = type
            override val displayName: Component = Component.text(displayNameStr)
            override val attributeModifiers: AttributeSet = attributeModifiers

            override fun checkRequirements(context: EquipmentCheckContext): Boolean {
                val table = EquipmentCheckContextClass().getNewTable(context)
                return checkRequirementsFunc.call(table).toboolean()
            }
        }
    }
}