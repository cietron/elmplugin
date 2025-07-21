package com.example.untitled.luaLoader

import com.example.untitled.Untitled
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaValue
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.JseBaseLib
import org.luaj.vm2.lib.jse.JseMathLib

class LuaGlobalFactory {

    companion object {
        val defaultServerGlobals: Globals = Globals()

        val defaultUserGlobals = Globals()

        init {
            defaultServerGlobals.load(JseBaseLib())
            defaultServerGlobals.load(PackageLib())
            defaultServerGlobals.load(StringLib())
            defaultServerGlobals.load(JseMathLib())
            LoadState.install(defaultServerGlobals)
            LuaC.install(defaultServerGlobals)
            LuaString.s_metatable = ReadOnlyLuaTable(LuaString.s_metatable)

            defaultUserGlobals.load(JseBaseLib())
            defaultUserGlobals.load(PackageLib())
            defaultUserGlobals.load(Bit32Lib())
            defaultUserGlobals.load(TableLib())
            defaultUserGlobals.load(StringLib())
            defaultUserGlobals.load(JseMathLib())
        }

        fun defaultUserGlobal(): LuaGlobalFactory {
            val factory = LuaGlobalFactory()

            // temporary solution.
            // this should be deep copying default user globals
            factory.addLibrary(JseBaseLib())
            factory.addLibrary(PackageLib())
            factory.addLibrary(Bit32Lib())
            factory.addLibrary(TableLib())
            factory.addLibrary(StringLib())
            factory.addLibrary(JseMathLib())

            return factory
        }
    }

    val userGlobals = Globals()

    fun addLibrary(library: LuaValue): LuaGlobalFactory {
        this.userGlobals.load(library)
        return this
    }

    fun makeChunk(script: String): LuaValue {
        return defaultServerGlobals.load(script, "main", this.userGlobals)
    }

    fun buildUserLibrary(): LuaGlobalFactory {

        val userLibStorage =
            Untitled.scriptManager.persistentStorage[ScriptManager.ScriptType.library]
        if (userLibStorage == null) {
            return this
        }

        val userLibraries = userLibStorage.entries

        for (a in userLibraries) {
            val libraryName = a.key
            val libraryContent = a.value

            val chunk = this.makeChunk(libraryContent)

            val preloadFunc =
                object : ZeroArgFunction() {
                    override fun call(): LuaValue? {
                        val table = chunk.call()
                        return table
                    }
                }

            this.userGlobals["package"]["preload"].set("usr/$libraryName", preloadFunc)
        }

        return this
    }

    fun build(): Globals {
        return this.userGlobals
    }
}
