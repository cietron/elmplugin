package com.example.untitled.luaAdapter.entity

import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.luaAdapter.util.BaseLuaTable
import com.example.untitled.luaAdapter.util.Vector3dTable
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

class SelectableEntityImplLua : BaseLuaTable<SelectableEntityImplLua.Container>(CLASS_NAME, true) {

    companion object {
        const val CLASS_NAME = "selectable_entity"
    }

    override fun modifyTable(
        table: LuaTable,
        container: Container
    ) {
        val impl = container.entity

        table.set("uuid", impl.uuid.toString())
        table.set("health", impl.health)
        table.set("mana", impl.mana)
        table.set("location", Vector3dTable().getTable(LuaTable(), Vector3dTable.Container(impl.position)))
        table.set(
            "normalizedFacingVector",
            Vector3dTable().getTable(LuaTable(), Vector3dTable.Container(impl.normalizedFacingVector))
        )
        table.set("isPlayer", LuaValue.valueOf(impl.isPlayer))

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

        table.set(
            "get_velocity",
            object : ZeroArgFunction() {
                override fun call(): LuaValue? {
                    val velocity = Vector3dTable.Container(impl.getVelocity())
                    return Vector3dTable().getTable(LuaTable(), velocity)
                }
            }
        )

        table.set(
            "set_velocity",
            object : OneArgFunction() {
                override fun call(arg: LuaValue?): LuaValue? {

                    val converted = Vector3dTable().fromLuaValue(arg)

                    converted ?: return NIL

                    impl.setVelocity(converted.vector)
                    return TRUE
                }
            }
        )
    }

    override fun checkParseTable(table: LuaTable): Boolean {
        TODO("Not yet implemented")
    }

    override fun fromTable(table: LuaTable): Container? {
        TODO("Not yet implemented")
    }

    data class Container(val entity: SelectableEntity)
}
