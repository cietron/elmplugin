package com.example.untitled.luaLoader

import com.example.untitled.Untitled
import com.example.untitled.luaAdapter.PlayMod
import com.example.untitled.luaApi.impl.PlayerModule

class LoadStartupScripts {
    companion object {
        fun load() {

            val env =
                LuaGlobalFactory.defaultUserGlobal()
                    .addLibrary(PlayMod())
                    .addLibrary(PlayerModule())
                    .buildUserLibrary()

            Untitled.scriptManager.persistentStorage[ScriptManager.ScriptType.startup]?.forEach { (filename, content) ->
                println(env.makeChunk(content).call())
                Untitled.instance.logger.info("Loaded startup script: $filename")
            }
        }
    }
}