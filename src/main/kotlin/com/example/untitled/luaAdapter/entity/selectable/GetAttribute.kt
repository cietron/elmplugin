package com.example.untitled.luaAdapter.entity.selectable

import com.example.untitled.api.attribute.AttributeSet
import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.luaAdapter.attribute.AttributeSetClass
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ZeroArgFunction

class GetAttribute(val impl: SelectableEntity) : ZeroArgFunction() {
    override fun call(): LuaValue? {
        return AttributeSetClass().getNewTable(impl.getAttribute() ?: AttributeSet.empty())
    }
}