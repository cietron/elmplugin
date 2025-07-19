package com.example.untitled.spell

import com.example.untitled.Untitled
import com.example.untitled.luaApi.impl.CaptureModule
import com.example.untitled.luaApi.impl.EntityModule
import com.example.untitled.luaApi.impl.EventModule
import com.example.untitled.luaApi.impl.PlayerModule
import com.example.untitled.luaLoader.LuaGlobalFactory
import com.example.untitled.luaLoader.ScriptManager
import net.kyori.adventure.text.TextComponent
import org.bukkit.event.player.PlayerInteractEvent

class spell {
    companion object {

        fun execute(event: PlayerInteractEvent) {

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

            val chunk =
                LuaGlobalFactory.defaultUserGlobal()
                    .addLibrary(PlayerModule(event.player))
                    .addLibrary(CaptureModule())
                    .addLibrary(EntityModule())
                    .addLibrary(EventModule())
                    .buildUserLibrary()
                    .makeChunk(script)

            chunk.call()
        }
    }
}
