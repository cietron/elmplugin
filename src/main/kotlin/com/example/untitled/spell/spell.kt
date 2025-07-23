package com.example.untitled.spell

import com.example.untitled.Untitled
import com.example.untitled.apiImpl.entity.EntityFactory
import com.example.untitled.apiImpl.entity.PlayerImpl
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
import org.bukkit.event.player.PlayerInteractEvent
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

object spell {
    fun execute(event: PlayerInteractEvent) {

        val spellObject = this.getScriptFromItemName(event)
        spellObject ?: return

        val player = EntityFactory.fromBukkitPlayer(event.player)

        if (Untitled.cooldownManager.isCoolingDown(player, spellObject.name)) {
            val remainingTicks = Untitled.cooldownManager.getRemainingTicks(player, spellObject.name)
            PlayerMessenger.sendCooldownMessage(player, spellObject.name, remainingTicks)
            return
        }

        val env = this.buildLuaEnvironment(event)
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
            PlayerImplBaseLua.Container(PlayerImpl(event.player.name, event.player.uniqueId))
        )

        val shouldExecute = this.check_requirement(requirements, LuaPlayer, spellObject.name)

        if (shouldExecute == null || !shouldExecute) {
            return
        }

        spellBody.call()

        Untitled.cooldownManager.store(player, spellObject.name, cooldown)
    }

    private fun buildLuaEnvironment(event: PlayerInteractEvent): LuaGlobalFactory {

        return LuaGlobalFactory.defaultUserGlobal()
            .addLibrary(PlayerModule())
            .addLibrary(CaptureModule())
            .addLibrary(EntityModule())
            .addLibrary(EventModule())
            .addLibrary(PlayMod())
            .addLibrary(SpellModule(event.player))
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

    private fun getScriptFromItemName(event: PlayerInteractEvent): SpellObject? {
        val displayName = event.item?.itemMeta?.displayName()

        if (displayName == null || displayName !is TextComponent) {
            return null
        }
        val spellname = displayName.content()

        if (
            Untitled.scriptManager.persistentStorage[ScriptManager.ScriptType.spell]!![
                spellname] == null
        ) {
            return null
        }

        val script =
            Untitled.scriptManager.persistentStorage[ScriptManager.ScriptType.spell]!![
                spellname]!!

        return SpellObject(spellname, script)
    }

    private data class SpellObject(val name: String, val content: String)
}
