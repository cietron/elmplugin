package com.example.untitled.luaAdapter.entity

import com.example.untitled.api.entity.SelectableEntity
import com.example.untitled.luaAdapter.util.Vector3dTable
import org.joml.Vector4d
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.VarArgFunction

class EntityEaseMoveLua(val impl: SelectableEntity) : VarArgFunction() {
    override fun invoke(args: Varargs?): Varargs? {
        if (args == null || args.narg() < 4) {
            return error("easedMove requires 4 arguments: startPoint (Vector3d), endPoint (Vector3d), bezierPoints (Vector4d), durationTick (integer)")
        }

        val startPointArg = args.arg(1)
        val endPointArg = args.arg(2)
        val bezierPointsArg = args.arg(3)
        val durationTickArg = args.arg(4)

        // Check types
        if (!startPointArg.istable()) {
            return error("easedMove: first argument must be a Vector3d table")
        }
        if (!endPointArg.istable()) {
            return error("easedMove: second argument must be a Vector3d table")
        }
        if (!bezierPointsArg.istable()) {
            return error("easedMove: third argument must be a Vector4d table")
        }
        if (!durationTickArg.isint()) {
            return error("easedMove: fourth argument must be an integer (durationTick)")
        }

        // Convert LuaTable to Vector3d
        val startPoint = Vector3dTable.toJoml(startPointArg.checktable())
            ?: return error("easedMove: invalid startPoint vector")
        val endPoint = Vector3dTable.toJoml(endPointArg.checktable())
            ?: return error("easedMove: invalid endPoint vector")

        // Convert LuaTable to Vector4d
        val bezierTable = bezierPointsArg.checktable()
        val check = { n: Int -> bezierTable.get(n).isnil() || !bezierTable.get(n).isnumber() }
        if (check(1) || check(2) || check(3) || check(4)) {
            return error("easedMove: bezierPoints must be a table of 4 numbers")
        }
        val bezierPoints = Vector4d(
            bezierTable.get(1).todouble(),
            bezierTable.get(2).todouble(),
            bezierTable.get(3).todouble(),
            bezierTable.get(4).todouble()
        )

        val durationTick = durationTickArg.toint()

        impl.easedMove(startPoint, endPoint, bezierPoints, durationTick)
        return LuaValue.NIL
    }
}