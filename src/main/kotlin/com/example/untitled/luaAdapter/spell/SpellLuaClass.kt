package com.example.untitled.luaAdapter.spell

import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.api.item.Equipment
import com.example.untitled.api.player.Player
import com.example.untitled.api.spell.Spell
import com.example.untitled.api.spell.SpellTriggerContext
import com.example.untitled.api.spell.SpellTriggerType
import com.example.untitled.luaAdapter.entity.selectable.SelectableEntityImplLua
import com.example.untitled.luaAdapter.item.ItemClass
import com.example.untitled.luaAdapter.player.PlayerImplBaseLua
import com.example.untitled.luaAdapter.util.BaseLuaTable
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaTable

/**
 * @custom.LuaDoc ---@class spell
 * @custom.LuaDoc ---@field identifier string
 * @custom.LuaDoc ---@field spellItem item
 * @custom.LuaDoc ---@field cooldownTicks integer
 * @custom.LuaDoc ---@field triggerType string # One of: "right_click", "double_shift", "hit_entity"
 * @custom.LuaDoc ---@field preCheck fun(caster: player, victim?: selectable_entity|player): boolean # For "hit_entity" trigger, both caster and victim are passed; for others, only caster.
 * @custom.LuaDoc ---@field execute fun(caster: player, victim?: selectable_entity|player) # For "hit_entity" trigger, both caster and victim are passed; for others, only caster.
 */
class SpellLuaClass() : BaseLuaTable<SpellLuaClass.Container>("SpellLuaClass", true) {

    override fun modifyTable(
        table: LuaTable,
        container: Container
    ) {
        TODO("Not yet implemented")
    }

    override fun checkParseTable(table: LuaTable): Boolean {
        val identifier = table.get("identifier")
        val spellItem = table.get("spellItem")
        val cooldownTicks = table.get("cooldownTicks")
        val triggerType = table.get("triggerType")
        val preCheckFunc = table.get("preCheck")
        val executeFunc = table.get("execute")
        4
        return !identifier.isnil() && identifier.isstring()
                && !cooldownTicks.isnil() && cooldownTicks.isint()
                && !triggerType.isnil() && triggerType.isstring()
                && !preCheckFunc.isnil() && preCheckFunc.isfunction()
                && !executeFunc.isnil() && executeFunc.isfunction()
                && spellItem.istable()
                && ItemClass().checkParseTable(spellItem as LuaTable)
    }

    override fun fromTable(table: LuaTable): Container? {
        if (!checkParseTable(table)) return null

        val identifier = table.get("identifier").tojstring()
        val spellItemValue = table.get("spellItem")
        val cooldownTicksValue = table.get("cooldownTicks").toint()
        val triggerTypeKey = table.get("triggerType").tojstring()
        val preCheckFunc = table.get("preCheck") as LuaFunction
        val executeFunc = table.get("execute") as LuaFunction

        val triggerType = SpellTriggerType.fromKey(triggerTypeKey)

        val spellItem = ItemClass().fromLuaValue(spellItemValue)!!

        triggerType ?: return null
        val spell = when (triggerType.key) {
            SpellTriggerType.HitEntity.key -> this.makeHitEntity(
                identifier,
                cooldownTicksValue,
                preCheckFunc,
                executeFunc,
                spellItem
            )

            SpellTriggerType.RightClick.key -> this.makeRightClick(
                identifier,
                cooldownTicksValue,
                preCheckFunc,
                executeFunc,
                spellItem
            )

            SpellTriggerType.DoubleShift.key -> this.makeDoubleShift(
                identifier,
                cooldownTicksValue,
                preCheckFunc,
                executeFunc,
                spellItem
            )

            SpellTriggerType.ArrowHitEntity.key -> this.makeArrowHitEntity(
                identifier,
                cooldownTicksValue,
                preCheckFunc,
                executeFunc,
                spellItem
            )

            else -> null
        }

        spell ?: return null

        return Container(spell)
    }

    private fun makeRightClick(
        identifier: String,
        cooldownTicks: Int,
        preCheck: LuaFunction,
        execute: LuaFunction,
        spellItem: Equipment
    ): Spell<SpellTriggerContext.RightClick> {
        return object : BaseSpell<SpellTriggerContext.RightClick>(identifier, cooldownTicks, spellItem) {
            override val triggerType: SpellTriggerType = SpellTriggerType.RightClick

            override fun preCheck(context: SpellTriggerContext.RightClick): Boolean {
                val player = context.player
                val table = PlayerImplBaseLua().getNewTable(PlayerImplBaseLua.Container(player))
                val result = preCheck.call(table)
                if (result.isnil() || !result.isboolean()) {
                    return false
                }
                return result.toboolean()
            }

            override fun execute(context: SpellTriggerContext.RightClick) {
                val player = context.player
                val table = PlayerImplBaseLua().getNewTable(PlayerImplBaseLua.Container(player))
                execute.call(table)
            }
        }
    }

    private fun makeDoubleShift(
        identifier: String,
        cooldownTicks: Int,
        preCheck: LuaFunction,
        execute: LuaFunction,
        spellItem: Equipment
    ): Spell<SpellTriggerContext.DoubleShift> {
        return object :
            BaseSpell<SpellTriggerContext.DoubleShift>(identifier, cooldownTicks, spellItem) {
            override val triggerType: SpellTriggerType = SpellTriggerType.DoubleShift

            override fun preCheck(context: SpellTriggerContext.DoubleShift): Boolean {
                val player = context.player
                val table = PlayerImplBaseLua().getNewTable(PlayerImplBaseLua.Container(player))
                val result = preCheck.call(table)
                if (result.isnil() || !result.isboolean()) {
                    return false
                }
                return result.toboolean()
            }

            override fun execute(context: SpellTriggerContext.DoubleShift) {
                val player = context.player
                val table = PlayerImplBaseLua().getNewTable(PlayerImplBaseLua.Container(player))
                execute.call(table)
            }
        }
    }

    private fun makeHitEntity(
        identifier: String,
        cooldownTicks: Int,
        preCheck: LuaFunction,
        execute: LuaFunction,
        spellItem: Equipment
    ): Spell<SpellTriggerContext.HitEntity> {
        return object : BaseSpell<SpellTriggerContext.HitEntity>(identifier, cooldownTicks, spellItem) {
            override val triggerType: SpellTriggerType = SpellTriggerType.HitEntity

            override fun preCheck(context: SpellTriggerContext.HitEntity): Boolean {
                val player = context.player
                val victim = context.victim

                val victimTable = when (victim) {
                    is Player -> PlayerImplBaseLua().getNewTable(PlayerImplBaseLua.Container(victim))
                    is SelectableEntity -> SelectableEntityImplLua().getNewTable(
                        SelectableEntityImplLua.Container(
                            victim
                        )
                    )
                }

                val playerTable = PlayerImplBaseLua().getNewTable(PlayerImplBaseLua.Container(player))
                val result = preCheck.call(playerTable, victimTable)
                if (result.isnil() || !result.isboolean()) {
                    return false
                }
                return result.toboolean()
            }

            override fun execute(context: SpellTriggerContext.HitEntity) {
                val player = context.player
                val table = PlayerImplBaseLua().getNewTable(PlayerImplBaseLua.Container(player))

                val victim = context.victim

                val victimTable = when (victim) {
                    is Player -> PlayerImplBaseLua().getNewTable(PlayerImplBaseLua.Container(victim))
                    is SelectableEntity -> SelectableEntityImplLua().getNewTable(
                        SelectableEntityImplLua.Container(
                            victim
                        )
                    )
                }
                execute.call(table, victimTable)
            }
        }
    }

    private fun makeArrowHitEntity(
        identifier: String,
        cooldownTicks: Int,
        preCheck: LuaFunction,
        execute: LuaFunction,
        spellItem: Equipment
    ): Spell<SpellTriggerContext.ArrowHitEntity> {
        return object :
            BaseSpell<SpellTriggerContext.ArrowHitEntity>(identifier, cooldownTicks, spellItem) {
            override val triggerType = SpellTriggerType.ArrowHitEntity

            override fun preCheck(context: SpellTriggerContext.ArrowHitEntity): Boolean {
                val player = context.player
                val victim = context.victim

                val victimTable = when (victim) {
                    is Player -> PlayerImplBaseLua().getNewTable(PlayerImplBaseLua.Container(victim))
                    is SelectableEntity -> SelectableEntityImplLua().getNewTable(
                        SelectableEntityImplLua.Container(
                            victim
                        )
                    )
                }

                val playerTable = PlayerImplBaseLua().getNewTable(PlayerImplBaseLua.Container(player))
                val result = preCheck.call(playerTable, victimTable)
                if (result.isnil() || !result.isboolean()) {
                    return false
                }
                return result.toboolean()
            }

            override fun execute(context: SpellTriggerContext.ArrowHitEntity) {
                val player = context.player
                val table = PlayerImplBaseLua().getNewTable(PlayerImplBaseLua.Container(player))

                val victim = context.victim

                val victimTable = when (victim) {
                    is Player -> PlayerImplBaseLua().getNewTable(PlayerImplBaseLua.Container(victim))
                    is SelectableEntity -> SelectableEntityImplLua().getNewTable(
                        SelectableEntityImplLua.Container(
                            victim
                        )
                    )
                }
                execute.call(table, victimTable)
            }
        }
    }

    private abstract class BaseSpell<T : SpellTriggerContext>(
        override val identifier: String,
        override val cooldownTicks: Int,
        override val spellItem: Equipment
    ) : Spell<T>


    data class Container(val spell: Spell<*>)
}

