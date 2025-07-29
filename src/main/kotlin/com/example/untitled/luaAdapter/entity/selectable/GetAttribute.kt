package com.example.untitled.luaAdapter.entity.selectable

import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.luaLoader.ReadOnlyLuaTable
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ZeroArgFunction

class GetAttribute(val impl: SelectableEntity) : ZeroArgFunction() {
    override fun call(): LuaValue? {
        val table = LuaTable()
        impl.getAttribute()?.forEach { (key, value) ->
            table.set(key, value)
        }

        return ReadOnlyLuaTable(table)
    }
}