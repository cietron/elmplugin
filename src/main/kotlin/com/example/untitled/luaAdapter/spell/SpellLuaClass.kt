package com.example.untitled.luaAdapter.spell

import com.example.untitled.api.player.Player
import com.example.untitled.api.spell.Spell
import com.example.untitled.luaAdapter.player.PlayerImplBaseLua
import com.example.untitled.luaAdapter.util.BaseLuaTable
import net.kyori.adventure.text.Component
import org.luaj.vm2.LuaTable

/**
 * @custom.LuaDoc ---@class spell
 * @custom.LuaDoc ---@field displayName string
 * @custom.LuaDoc ---@field description string
 * @custom.LuaDoc ---@field cooldownTicks integer
 * @custom.LuaDoc ---@field preCheck fun(caster: player): boolean # Interpreted to false if the function returns a non-boolean value
 * @custom.LuaDoc ---@field execute fun(caster: player)
 */
class SpellLuaClass : BaseLuaTable<SpellLuaClass.Container>("SpellLuaClass", true) {

    override fun modifyTable(
        table: LuaTable,
        container: Container
    ) {
        TODO("Not yet implemented")
    }

    override fun checkParseTable(table: LuaTable): Boolean {
        val displayName = table.get("displayName")
        val description = table.get("description")
        val cooldownTicks = table.get("cooldownTicks")
        val preCheckFunc = table.get("preCheck")
        val executeFunc = table.get("execute")

        return !displayName.isnil() && displayName.isstring()
                && !description.isnil() && description.isstring()
                && !cooldownTicks.isnil() && cooldownTicks.isint()
                && !preCheckFunc.isnil() && preCheckFunc.isfunction()
                && !executeFunc.isnil() && executeFunc.isfunction()
    }

    override fun fromTable(table: LuaTable): Container? {
        if (!checkParseTable(table)) return null

        val displayNameValue = table.get("displayName")
        val descriptionValue = table.get("description")
        val cooldownTicksValue = table.get("cooldownTicks")
        val preCheckFunc = table.get("preCheck")
        val executeFunc = table.get("execute")

        val spell = object : Spell {
            override val displayName: Component =
                Component.text(displayNameValue.tojstring())
            override val description: Component =
                Component.text(descriptionValue.tojstring())
            override val cooldownTicks: Int = cooldownTicksValue.toint()

            override fun preCheck(player: Player): Boolean {
                val table = PlayerImplBaseLua().getNewTable(PlayerImplBaseLua.Container(player))
                val result = preCheckFunc.call(table)
                if (result.isnil() || !result.isboolean()) {
                    return false
                }
                return result.toboolean()
            }

            override fun execute(player: Player) {
                val table = PlayerImplBaseLua().getNewTable(PlayerImplBaseLua.Container(player))
                executeFunc.call(table)
            }
        }

        return Container(spell)
    }

    data class Container(val spell: Spell)
}

