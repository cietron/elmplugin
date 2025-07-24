package com.example.untitled.spell

import com.example.untitled.Untitled
import com.example.untitled.apiImpl.entity.EntityFactory
import com.example.untitled.luaAdapter.PlayMod
import com.example.untitled.luaAdapter.SpellModule
import com.example.untitled.luaAdapter.player.PlayerImplBaseLua
import com.example.untitled.luaApi.impl.CaptureModule
import com.example.untitled.luaApi.impl.EntityModule
import com.example.untitled.luaApi.impl.EventModule
import com.example.untitled.luaApi.impl.PlayerModule
import com.example.untitled.luaLoader.LuaGlobalFactory
import com.example.untitled.luaLoader.ScriptManager
import com.example.untitled.player.PlayerMessenger
import net.kyori.adventure.text.TextComponent
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

object spell {
    fun execute(spellName: String, bukkitPlayer: Player) {

        val spellObject = this.getScriptFromItemName(spellName)
        spellObject ?: return

        val player = EntityFactory.fromBukkitPlayer(bukkitPlayer)

        if (Untitled.cooldownManager.isCoolingDown(player, spellObject.name)) {
            val remainingTicks = Untitled.cooldownManager.getRemainingTicks(player, spellObject.name)
            PlayerMessenger.sendCooldownMessage(player, spellObject.name, remainingTicks)
            return
        }

        val env = this.buildLuaEnvironment(bukkitPlayer)
        val chunk = env.makeChunk(spellObject.content)

        chunk.call()

        val spellInfo = env.userGlobals.get("spell_info")
        if (!spellInfo.istable()) {
            Untitled.instance.logger.warning("No spell_info table found in script: ${spellObject.name}")

            return
        }

        val description = spellInfo.get("description").tojstring() ?: "NO_DESCRIPTION"
        val cooldown = spellInfo.get("cooldown").toint() // null = 0
        val requirements = spellInfo.get("requirements")
        val spellBody = spellInfo.get("body")

        val LuaPlayer = PlayerImplBaseLua().getTable(
            LuaTable(),
            PlayerImplBaseLua.Container(EntityFactory.fromBukkitPlayer(bukkitPlayer))
        )

        val shouldExecute = this.check_requirement(requirements, LuaPlayer, spellObject.name)

        if (shouldExecute == null || !shouldExecute) {
            return
        }

        spellBody.call()

        Untitled.cooldownManager.store(player, spellObject.name, cooldown)
    }

    private fun buildLuaEnvironment(bukkitPlayer: Player): LuaGlobalFactory {

        return LuaGlobalFactory.defaultUserGlobal()
            .addLibrary(PlayerModule())
            .addLibrary(CaptureModule())
            .addLibrary(EntityModule())
            .addLibrary(EventModule())
            .addLibrary(PlayMod())
            .addLibrary(SpellModule(bukkitPlayer))
            .buildUserLibrary()
    }

    private fun check_requirement(requirements: LuaValue, LuaPlayer: LuaTable, spellname: String): Boolean? {
        if (!requirements.isfunction()) {
            Untitled.instance.logger.warning("Spell $spellname does not have a valid requirement function")
            return null
        }

        val requirementResult = requirements.call(LuaPlayer)

        if (!requirementResult.isboolean()) {
            Untitled.instance.logger.warning("Spell $spellname requirement does not return boolean")
            return null
        }

        return requirementResult.toboolean()
    }

    private fun getScriptFromItemName(spellName: String): SpellObject? {
        if (
            Untitled.scriptManager.persistentStorage[ScriptManager.ScriptType.spell]!![
                spellName] == null
        ) {
            return null
        }

        val script =
            Untitled.scriptManager.persistentStorage[ScriptManager.ScriptType.spell]!![
                spellName]!!

        return SpellObject(spellName, script)
    }

    fun parseSpellname(itemStack: ItemStack): String? {
        val displayName = itemStack.itemMeta?.displayName()

        if (displayName == null || displayName !is TextComponent) {
            return null
        }
        val spellname = displayName.content()
        return spellname
    }

    private data class SpellObject(val name: String, val content: String)
}
