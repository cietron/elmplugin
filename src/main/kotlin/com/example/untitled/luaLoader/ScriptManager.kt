package com.example.untitled.luaLoader

import java.io.File
import java.nio.file.Files

class ScriptManager() {
    val storage = HashMap<ScriptType, MutableMap<String, File>>()
    val persistentStorage = HashMap<ScriptType, MutableMap<String, String>>()
    var loader: ScriptLoader? = null

    fun reload() {
        this.clean()
        if (this.loader == null) {
            println("ScriptManager reload failed: ScriptLoader does not exist.")
            return
        }

        this.loader!!.load(this)
    }

    fun clean() {
        this.storage.clear()
        this.persistentStorage.clear()
    }

    fun loadScript(file: File, scriptType: ScriptType) {
        if (scriptType == ScriptType.none) {
            println("Script type not found. Ignoring script file : ${file.name}")
            return
        }
        if (scriptType.persistent) {
            this.loadPersistent(file, scriptType)
        } else {
            this.loadNormal(file, scriptType)
        }
    }

    private fun loadPersistent(file: File, scriptType: ScriptType) {
        val content = Files.readAllBytes(file.toPath()).toString(Charsets.UTF_8)
        val filename = file.nameWithoutExtension

        if (this.persistentStorage.get(scriptType) == null) {
            this.persistentStorage[scriptType] = makePersistentStorage()
        }

        this.persistentStorage[scriptType]!!.put(filename, content)
    }

    private fun loadNormal(file: File, scriptType: ScriptType) {
        val filename = file.nameWithoutExtension

        if (this.storage[scriptType] == null) {
            this.storage[scriptType] = makeStorage()
        }

        this.storage[scriptType]!!.put(filename, file)
    }

    private fun makePersistentStorage(): HashMap<String, String> {
        return HashMap()
    }

    private fun makeStorage(): HashMap<String, File> {
        return HashMap()
    }

    enum class ScriptType(val persistent: Boolean) {
        spell(true),
        item(false),
        library(true),
        startup(true),
        none(false),
    }
}
