package com.example.untitled.luaAdapter.util

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

abstract class BaseLuaTable(val className: String) {

    protected abstract fun modifyTable(table: LuaTable)

    fun getTable(table: LuaTable): LuaTable {
        if (table.get("__class") == LuaValue.NIL) {
            table.set("__class", LuaTable()) // class array
        }

        val classArray = table.get("__class")

        classArray.set(classArray.length() + 1, className)

        this.modifyTable(table)

        return table
    }

    companion object {
        fun isInstanceOf(table: LuaTable, className: String): Boolean {
            for (i in 1..table.length()) {
                if (table.get(i)?.tojstring() == className) {
                    return true
                }
            }
            return false
        }
    }
}
