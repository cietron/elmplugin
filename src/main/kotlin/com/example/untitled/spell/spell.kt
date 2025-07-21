package com.example.untitled.spell

import com.example.untitled.Untitled
import com.example.untitled.apiImpl.entity.PlayerImpl
import com.example.untitled.luaAdapter.PlayMod
import com.example.untitled.luaAdapter.player.PlayerImplBaseLua
import com.example.untitled.luaApi.impl.CaptureModule
import com.example.untitled.luaApi.impl.EntityModule
import com.example.untitled.luaApi.impl.EventModule
import com.example.untitled.luaApi.impl.PlayerModule
import com.example.untitled.luaLoader.LuaGlobalFactory
import com.example.untitled.luaLoader.ScriptManager
import net.kyori.adventure.text.TextComponent
import org.bukkit.event.player.PlayerInteractEvent
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

class spell {
    companion object {

        fun execute(event: PlayerInteractEvent) {

            if (event.player.hasCooldown(event.item!!)) {
                return
            }

            val displayName = event.item?.itemMeta?.displayName()

            if (displayName == null || displayName !is TextComponent) {
                return
            }
            val spellname = displayName.content()

            if (
                Untitled.scriptManager.persistentStorage[ScriptManager.ScriptType.spell]!![
                    spellname] == null
            ) {
                return
            }

            val script =
                Untitled.scriptManager.persistentStorage[ScriptManager.ScriptType.spell]!![
                    spellname]!!

            val env =
                LuaGlobalFactory.defaultUserGlobal()
                    .addLibrary(PlayerModule(event.player))
                    .addLibrary(CaptureModule())
                    .addLibrary(EntityModule())
                    .addLibrary(EventModule())
                    .addLibrary(PlayMod(event.player))
                    .buildUserLibrary()

            val chunk = env.makeChunk(script)

            chunk.call()

            val spellInfo = env.userGlobals.get("spell_info")
            if (!spellInfo.istable()) {
                Untitled.instance.logger.warning("No spell_info table found in script: $spellname")

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

            val shouldExecute = this.check_requirement(requirements, LuaPlayer, spellname)

            if (shouldExecute == null || !shouldExecute) {
                return
            }

            spellBody.call()

//            chunk.call()

            event.item
            event.player.setCooldown(event.item!!, cooldown)
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
    }
}
