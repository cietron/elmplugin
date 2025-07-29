package com.example.untitled.luaLoader

import com.example.untitled.Untitled
import com.example.untitled.luaApi.impl.PlayerModule
import com.example.untitled.luaLoader.luaModules.PlayMod
import com.example.untitled.luaLoader.luaModules.ServerModule
import com.example.untitled.luaLoader.luaModules.SpellModule

object LoadStartupScripts {
    fun load() {

        val env =
            LuaGlobalFactory.defaultUserGlobal()
                .addLibrary(PlayMod())
                .addLibrary(PlayerModule())
                .addLibrary(ServerModule())
                .addLibrary(SpellModule())
                .buildUserLibrary()

        Untitled.scriptManager.persistentStorage[ScriptManager.ScriptType.startup]?.forEach { (filename, content) ->
            env.makeChunk(content).call()
            Untitled.instance.logger.info("Loaded startup script: $filename")
        }
    }


    fun loadServer() {
        val env =
            LuaGlobalFactory.defaultUserGlobal()
                .addLibrary(PlayMod())
                .addLibrary(PlayerModule())
                .addLibrary(ServerModule())
                .addLibrary(SpellModule())
                .buildUserLibrary()

        Untitled.scriptManager.persistentStorage[ScriptManager.ScriptType.server]?.forEach { (filename, content) ->
            env.makeChunk(content).call()
            Untitled.instance.logger.info("Loaded server script: $filename")
        }
    }
}