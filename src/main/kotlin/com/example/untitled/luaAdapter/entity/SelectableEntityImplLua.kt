package com.example.untitled.luaAdapter.entity

import com.example.untitled.apiImpl.entity.SelectableEntityImpl
import com.example.untitled.luaAdapter.util.BaseLuaTable
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction

class SelectableEntityImplLua(val impl: SelectableEntityImpl) : BaseLuaTable(CLASS_NAME) {

    companion object {
        const val CLASS_NAME = "selectable_entity"
    }

    override fun modifyTable(table: LuaTable) {
        table.set("uuid", impl.uuid.toString())
        table.set("health", impl.health)
        table.set("mana", impl.mana)

        // Lua: entity:get_attribute("attr_name")
        table.set(
            "get_attribute",
            object : OneArgFunction() {
                override fun call(arg: LuaValue): LuaValue {
                    val attr = arg.checkjstring()
                    val value = impl.getAttribute(attr)
                    return when (value) {
                        is com.example.untitled.storage.SafeAttributeValue.IntValue ->
                            LuaValue.valueOf(value.value)
                        is com.example.untitled.storage.SafeAttributeValue.DoubleValue ->
                            LuaValue.valueOf(value.value)
                        is com.example.untitled.storage.SafeAttributeValue.StringValue ->
                            LuaValue.valueOf(value.value)
                        else -> LuaValue.NIL
                    }
                }
            },
        )

        // Lua: entity:set_attribute("attr_name", value)
        table.set(
            "set_attribute",
            object : TwoArgFunction() {
                override fun call(attrName: LuaValue, value: LuaValue): LuaValue {
                    val attr = attrName.checkjstring()
                    val stored =
                        when {
                            value.isint() -> impl.setAttribute(attr, value.toint())
                            value.isnumber() -> impl.setAttribute(attr, value.todouble())
                            value.isstring() -> impl.setAttribute(attr, value.tojstring())
                            else -> false
                        }
                    return LuaValue.valueOf(stored)
                }
            },
        )
    }
}
