package com.example.untitled.luaLoader

import com.example.untitled.luaLoader.LuaRunner.ReadOnlyLuaTable
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaValue
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.Bit32Lib
import org.luaj.vm2.lib.PackageLib
import org.luaj.vm2.lib.StringLib
import org.luaj.vm2.lib.TableLib
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

    fun build(): Globals {
        return this.userGlobals
    }
}
