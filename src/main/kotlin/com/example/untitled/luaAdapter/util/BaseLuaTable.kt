package com.example.untitled.luaAdapter.util

import com.example.untitled.luaLoader.ReadOnlyLuaTable
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

abstract class BaseLuaTable<T>(val className: String, val readOnly: Boolean) {

    protected abstract fun modifyTable(table: LuaTable, container: T)

    fun getTable(table: LuaTable, container: T): LuaTable {
        if (table.get("__class") == LuaValue.NIL) {
            table.set("__class", LuaTable()) // class array
        }

        val classArray = table.get("__class")

        classArray.set(classArray.length() + 1, className)
        // I'm not sure if this is pass by reference or value
        table.set("__class", classArray)

        this.modifyTable(table, container)

        return when (readOnly) {
            true -> ReadOnlyLuaTable(table)
            false -> table
        }
    }

    fun getNewTable(container: T): LuaTable {
        return this.getTable(LuaTable(), container)
    }

    abstract fun checkParseTable(table: LuaTable): Boolean

    protected abstract fun fromTable(table: LuaTable): T?

    fun fromLuaValue(value: LuaValue?): T? {

        if (value != null && value.istable() && this.checkParseTable(value as LuaTable)) {
            return this.fromTable(value)
        }

        return null
    }

    companion object {
        fun isInstanceOf(table: LuaTable, className: String): Boolean {
            val classArray = table.get("__class")
            if (!classArray.istable()) return false
            for (i in 1..classArray.length()) {
                if (table.get(i)?.tojstring() == className) {
                    return true
                }
            }
            return false
        }

    }
}
