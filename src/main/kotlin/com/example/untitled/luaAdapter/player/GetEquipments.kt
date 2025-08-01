package com.example.untitled.luaAdapter.player

import com.example.untitled.api.player.Player
import com.example.untitled.luaAdapter.item.ItemClass
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ZeroArgFunction

class GetEquipments(val player: Player) : ZeroArgFunction() {
    override fun call(): LuaValue? {
        val equipments = player.getEquipments().map { equipment -> ItemClass().getNewTable(equipment) }.toTypedArray()
        return LuaValue.listOf(equipments)
    }
}