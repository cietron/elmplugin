package com.example.untitled.luaAdapter.attribute

import com.example.untitled.api.attribute.Attribute
import com.example.untitled.api.attribute.AttributeSet
import com.example.untitled.luaAdapter.util.BaseLuaTable
import org.luaj.vm2.LuaTable

/**
 * @custom.LuaDoc ---@class attributeSet
 * @custom.LuaDoc ---@field [string] number # Each key is an attribute identifier, value is its number value
 * @custom.LuaDoc ---@desc Represents a set of vanilla and script registered attributes.
 * @custom.LuaDoc local attributeSet = {}
 */
class AttributeSetClass : BaseLuaTable<AttributeSet>("attributeSet", true) {
    override fun modifyTable(table: LuaTable, container: AttributeSet) {
        for (attr in container.values()) {
            table.set(attr.identifier, attr.value)
        }
    }

    override fun checkParseTable(table: LuaTable): Boolean {
        val keys = table.keys()
        for (key in keys) {
            if (!key.isstring()) return false
            val value = table.get(key)
            if (!value.isnumber()) return false
        }
        return true
    }

    override fun fromTable(table: LuaTable): AttributeSet? {
        val attributes = mutableListOf<Attribute>()
        val keys = table.keys()
        for (key in keys) {
            val identifier = key.tojstring()
            val value = table.get(key).todouble()
            attributes.add(Attribute(identifier, value))
        }
        return AttributeSet.fromList(attributes)
    }
}