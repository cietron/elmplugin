package com.example.untitled.luaAdapter.item

import com.example.untitled.api.item.Equipment
import com.example.untitled.apiImpl.store.Repository
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction

/**
 * @custom.LuaDoc ---@desc Registers a custom equipment item to the equipment repository.
 * @custom.LuaDoc ---@param item1 item # The item to register
 * @custom.LuaDoc function ServerLib.registerItem(item1) end
 */
class RegisterItem(val equipmentRepository: Repository<Equipment>) : OneArgFunction() {
    override fun call(arg: LuaValue): LuaValue? {
        val equipment = ItemClass().fromLuaValue(arg) ?: return null
        equipmentRepository.register(equipment.identifier, equipment)
        return NIL
    }
}