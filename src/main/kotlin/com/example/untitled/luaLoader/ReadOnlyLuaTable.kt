/*
 * From luaJ example code
 * https://github.com/luaj/luaj/blob/master/examples/jse/SampleSandboxed.java
 *
 * MIT License
 * Copyright (c) 2007 LuaJ. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.example.untitled.luaLoader

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

class ReadOnlyLuaTable(table: LuaValue) : LuaTable() {
    init {
        presize(table.length(), 0)
        var n = table.next(NIL)
        while (!n.arg1().isnil()) {
            val key = n.arg1()
            val value = n.arg(2)
            super.rawset(key, if (value.istable()) ReadOnlyLuaTable(value) else value)
            n = table.next(n.arg1())
        }
    }

    override fun setmetatable(metatable: LuaValue): LuaValue {
        return error("table is read-only")
    }

    override fun set(key: Int, value: LuaValue) {
        error("table is read-only")
    }

    override fun rawset(key: Int, value: LuaValue) {
        error("table is read-only")
    }

    override fun rawset(key: LuaValue, value: LuaValue) {
        error("table is read-only")
    }

    override fun remove(pos: Int): LuaValue {
        return error("table is read-only")
    }
}