/*
 * From luaJ example code
 * https://github.com/luaj/luaj/blob/master/examples/jse/SampleSandboxed.java
 *
 * MIT License
 * Copyright (c) 2007 LuaJ. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.example.untitled.luaLoader

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

class LuaRunner
    // To load scripts, we occasionally need a math library in addition to compiler support.
    // To limit scripts using the debug library, they must be closures, so we only install LuaC.

    // Set up the LuaString metatable to be read-only since it is shared across all scripts.
{

    // Create server globals with just enough library support to compile user scripts.
    private val serverGlobals: Globals = Globals()

    init {
        serverGlobals.load(JseBaseLib())
        serverGlobals.load(PackageLib())
        serverGlobals.load(StringLib())
        serverGlobals.load(JseMathLib())
        LoadState.install(serverGlobals)
        LuaC.install(serverGlobals)
        LuaString.s_metatable = ReadOnlyLuaTable(LuaString.s_metatable)
    }

    fun sandboxedRun(script: String) {
        val userGlobals = Globals()
        userGlobals.load(JseBaseLib())
        userGlobals.load(PackageLib())
        userGlobals.load(Bit32Lib())
        userGlobals.load(TableLib())
        userGlobals.load(StringLib())
        userGlobals.load(JseMathLib())

        val chunk: LuaValue = serverGlobals.load(script, "main", userGlobals)
        chunk.call()
    }

}
