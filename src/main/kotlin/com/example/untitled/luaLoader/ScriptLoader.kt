package com.example.untitled.luaLoader

import java.io.File

class ScriptLoader(val pluginDirectory: File) {
    fun load(scriptManager: ScriptManager) {
        createFolders()

        val scriptDir = pluginDirectory.resolve("scripts")

        for (dir in scriptDir.listFiles({ file -> file.isDirectory })) {
            for (scriptFile in dir.listFiles({ file -> file.extension == "lua" })) {
                val scriptType =
                    when (dir.name) {
                        "spells" -> ScriptManager.ScriptType.spell
                        "items" -> ScriptManager.ScriptType.item
                        "lib" -> ScriptManager.ScriptType.library
                        "startup" -> ScriptManager.ScriptType.startup
                        else -> ScriptManager.ScriptType.none
                    }

                scriptManager.loadScript(scriptFile, scriptType)
            }
        }
    }

    fun createFolders() {
        val scriptDirectory = pluginDirectory.resolve("scripts")
        scriptDirectory.mkdirs()
        scriptDirectory.resolve("spells").mkdirs()
        scriptDirectory.resolve("startup").mkdirs()
        scriptDirectory.resolve("items").mkdirs()
    }
}
