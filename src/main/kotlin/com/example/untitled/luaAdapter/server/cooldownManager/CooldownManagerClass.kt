package com.example.untitled.luaAdapter.server.cooldownManager

import com.example.untitled.api.spell.CooldownManager
import com.example.untitled.luaAdapter.util.BaseLuaTable
import org.luaj.vm2.LuaTable

/**
 * @custom.LuaDoc ---@class cooldownManager
 * @custom.LuaDoc ---@field store fun(player: player, spellIdentifier: string, durationTicks: integer): nil
 * @custom.LuaDoc ---@field isCoolingDown fun(player: player, spellIdentifier: string): boolean
 * @custom.LuaDoc ---@field getRemainingTicks fun(player: player, spellIdentifier: string): integer
 * @custom.LuaDoc local cooldownManager = {}
 */
class CooldownManagerClass : BaseLuaTable<CooldownManager>("cooldownManager", true) {
    override fun modifyTable(table: LuaTable, container: CooldownManager) {
        table.set("store", Store(container))
        table.set("isCoolingDown", IsCoolingDown(container))
        table.set("getRemainingTicks", GetRemainingTicks(container))
    }

    override fun checkParseTable(table: LuaTable): Boolean {
        TODO("Currently not needed")
    }

    override fun fromTable(table: LuaTable): CooldownManager? {
        TODO("Currently not needed")
    }
}