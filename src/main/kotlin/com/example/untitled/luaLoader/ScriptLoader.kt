package com.example.untitled.luaLoader

import java.io.File

class ScriptLoader(val pluginDirectory: File) {
    fun load(scriptManager: ScriptManager) {
        createFolders()

        val scriptDir = pluginDirectory.resolve("scripts")

        for (dir in scriptDir.listFiles({ file -> file.isDirectory })) {
            val scriptType =
                when (dir.name) {
                    "spells" -> ScriptManager.ScriptType.spell
                    "items" -> ScriptManager.ScriptType.item
                    "lib" -> ScriptManager.ScriptType.library
                    "startup" -> ScriptManager.ScriptType.startup
                    "server" -> ScriptManager.ScriptType.server
                    else -> ScriptManager.ScriptType.none
                }

            val scriptFiles = dir.walkTopDown()
                .filter { file -> file.isFile && file.extension == "lua" && file.absolutePath.startsWith(scriptDir.absolutePath) }

            for (scriptFile in scriptFiles) {
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
        scriptDirectory.resolve("server").mkdirs()
        scriptDirectory.resolve("lib").mkdirs()
    }
}
