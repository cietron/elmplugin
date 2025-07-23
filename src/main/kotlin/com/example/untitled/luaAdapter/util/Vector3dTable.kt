package com.example.untitled.luaAdapter.util

import org.joml.Vector3d
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction

/**
 * @custom.LuaDoc ---@class vector3d
 * @custom.LuaDoc ---@field x number
 * @custom.LuaDoc ---@field y number
 * @custom.LuaDoc ---@field z number
 * @custom.LuaDoc ---@field mul fun(self: vector3d, scalar: number): vector3d
 * @custom.LuaDoc ---@field add fun(self: vector3d, other: vector3d): vector3d
 * @custom.LuaDoc ---@field copy fun(self: vector3d): vector3d
 */
class Vector3dTable : BaseLuaTable<Vector3dTable.Container>(CLASS_NAME, false) {
    companion object {

        const val CLASS_NAME = "vector3d"

        fun toJoml(table: LuaTable): Vector3d? {
            val check = { n: Int -> table.get(n).isnil() || !table.get(n).isnumber() }
            if (check(1) || check(2) || check(3)) {
                return null
            }

            val c = { n: Int -> table.get(n).todouble() }

            return Vector3d(c(1), c(2), c(3))
        }
    }

    override fun modifyTable(
        table: LuaTable,
        container: Container
    ) {

        val vector = container.vector

        table.set(1, LuaValue.valueOf(vector.x))
        table.set(2, LuaValue.valueOf(vector.y))
        table.set(3, LuaValue.valueOf(vector.z))

        table.set("mul", object : TwoArgFunction() {
            override fun call(arg1: LuaValue, arg2: LuaValue?): LuaValue? {
                if (arg2 == null || !arg2.isnumber()) {
                    return error("Vector scalar multiplied by non-numbers: $arg2")
                }

                val scalar = arg2.todouble()

                arg1.set(1, LuaValue.valueOf(arg1.get(1).todouble() * scalar))
                arg1.set(2, LuaValue.valueOf(arg1.get(2).todouble() * scalar))
                arg1.set(3, LuaValue.valueOf(arg1.get(3).todouble() * scalar))
                return arg1
            }
        })

        table.set("copy", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue? {
                val vector = Vector3dTable().fromLuaValue(arg)

                vector ?: return error("Failed to copy vector")

                return Vector3dTable().getTable(LuaTable(), vector)
            }
        })

        table.set("add", object : TwoArgFunction() {
            override fun call(arg1: LuaValue, arg2: LuaValue?): LuaValue? {
                if (arg2 == null || !arg2.istable()) {
                    return error("Vector add expects another vector3d table as the second argument")
                }
                val v1 = Vector3dTable.toJoml(arg1.checktable())
                    ?: return error("First argument is not a valid vector3d table")
                val v2 = Vector3dTable.toJoml(arg2.checktable())
                    ?: return error("Second argument is not a valid vector3d table")

                val result = Vector3d(v1).add(v2)
                return Vector3dTable().getTable(LuaTable(), Vector3dTable.Container(result))
            }
        })
    }

    override fun checkParseTable(table: LuaTable): Boolean {

        val check = { n: Int -> !table.get(n).isnil() && table.get(n).isnumber() }
        return check(1) && check(2) && check(3)
    }

    override fun fromTable(table: LuaTable): Container? {

        val c = { n: Int -> table.get(n).todouble() }

        return Container(Vector3d(c(1), c(2), c(3)))
    }

    data class Container(val vector: Vector3d)
}