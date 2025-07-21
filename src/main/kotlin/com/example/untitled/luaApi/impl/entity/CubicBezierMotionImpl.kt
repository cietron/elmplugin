package com.example.untitled.luaApi.impl.entity

import com.example.untitled.Untitled
import com.example.untitled.luaApi.api.entity.CubicBezierMotion
import com.example.untitled.luaLoader.EventManager
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import org.joml.Vector3d
import org.luaj.vm2.*
import org.luaj.vm2.lib.VarArgFunction
import java.util.*
import kotlin.math.abs
import kotlin.math.floor

class CubicBezierMotionImpl : CubicBezierMotion, VarArgFunction() {
    override fun move(
        startPoint: LuaTable,
        endPoint: LuaTable,
        bezierCurve: LuaTable,
        duration: LuaNumber,
        entityUUID: LuaString,
    ): LuaValue {

        val convert = { table: LuaTable, n: Int -> table.get(n)!!.todouble() }
        val convertVector = { table: LuaTable ->
            Vector3d(
                table.get(1)!!.todouble(),
                table.get(2)!!.todouble(),
                table.get(3)!!.todouble(),
            )
        }

        val easing =
            CubicBezierEasing(
                convert(bezierCurve, 1),
                convert(bezierCurve, 2),
                convert(bezierCurve, 3),
                convert(bezierCurve, 4),
            )

        val startVec = convertVector(startPoint)
        val endVec = convertVector(endPoint)
        val velocityDirection = Vector3d(endVec).sub(startVec).normalize()

        val stopTick = floor(duration.todouble() * 20).toInt()

        var tick = 0

        Untitled.eventManager.addJavaListener(
            EventManager.EventType.onTick,
            {
                val entity =
                    Bukkit.getServer().getEntity(UUID.fromString(entityUUID.tojstring()))
                            as LivingEntity?

                entity ?: return@addJavaListener true

                if (tick >= stopTick) {
                    entity.velocity = Vector(0, 0, 0)
                    return@addJavaListener true
                }

                val currentT = tick / stopTick.toDouble()
                val nextT = ((tick + 1).coerceAtMost(stopTick - 1)) / stopTick.toDouble()

                val posNow = Vector3d(startVec).lerp(endVec, easing.ease(currentT))
                val posNext = Vector3d(startVec).lerp(endVec, easing.ease(nextT))

                val velocity = posNext.sub(posNow)
                entity.velocity = Vector(velocity.x, velocity.y, velocity.z)

                //            val progress = easing.ease(tick/stopTick)
                //            val velocityProgress = easing.velocity(tick/stopTick)
                //
                //            val pos = startVec.lerp(endVec, progress)
                //
                //            val velocity = Vector3d(velocityDirection).mul(velocityProgress)
                ////            entity.teleport(Location(entity.world, pos.x, pos.y, pos.z,
                // entity.yaw, entity.pitch))
                //
                //            entity.velocity = Vector(velocity.x, velocity.y, velocity.z)

                tick += 1
                return@addJavaListener false
            },
        )

        return NIL
    }

    override fun invoke(args: Varargs?): Varargs? {

        if (args == null || args.narg() < 4) {
            return error("CubicBezier incorrect parameter count.")
        }

        val startPoint = args.arg(1)
        val endPoint = args.arg(2)
        val bezierCurve = args.arg(3)
        val duration = args.arg(4)
        val entityUUID = args.arg(5)

        if (
            !startPoint.istable() ||
                !endPoint.istable() ||
                !bezierCurve.istable() ||
                !duration.isnumber() ||
                !entityUUID.isstring()
        ) {
            return error("CubicBezier incorrect parameter type.")
        }

        return this.move(
            startPoint as LuaTable,
            endPoint as LuaTable,
            bezierCurve as LuaTable,
            duration as LuaNumber,
            entityUUID!! as LuaString,
        )
    }

    class CubicBezierEasing(
        private val x1: Double,
        private val y1: Double,
        private val x2: Double,
        private val y2: Double,
    ) {
        fun ease(t: Double): Double {
            return bezierY(solveCurveX(t))
        }

        fun velocity(t: Double): Double {
            val s = solveCurveX(t) // Find s such that u(s) = t
            val dy = bezierYDerivative(s)
            val dx = bezierXDerivative(s)
            return dy / dx // Chain rule: dv/dt = (dv/ds) / (du/ds)
        }

        private fun bezierX(s: Double): Double {
            return cubicBezier(s, 0.0, x1, x2, 1.0)
        }

        private fun bezierY(s: Double): Double {
            return cubicBezier(s, 0.0, y1, y2, 1.0)
        }

        private fun cubicBezier(s: Double, p0: Double, p1: Double, p2: Double, p3: Double): Double {
            val u = 1 - s
            return (u * u * u * p0 + 3 * u * u * s * p1 + 3 * u * s * s * p2 + s * s * s * p3)
        }

        private fun bezierXDerivative(s: Double): Double {
            return cubicBezierDerivative(s, 0.0, x1, x2, 1.0)
        }

        private fun bezierYDerivative(s: Double): Double {
            return cubicBezierDerivative(s, 0.0, y1, y2, 1.0)
        }

        private fun cubicBezierDerivative(
            s: Double,
            p0: Double,
            p1: Double,
            p2: Double,
            p3: Double,
        ): Double {
            val u = 1 - s
            return (3 * u * u * (p1 - p0) + 6 * u * s * (p2 - p1) + 3 * s * s * (p3 - p2))
        }

        private fun solveCurveX(x: Double): Double {
            var s = x
            for (i in 0..19) {
                val xEst = bezierX(s)
                val dx = xEst - x
                if (abs(dx) < 1e-6) return s
                val dEst = bezierXDerivative(s)
                if (abs(dEst) < 1e-6) break
                s -= dx / dEst
            }

            // fallback to binary search
            var min = 0.0
            var max = 1.0
            s = x
            while (max - min > 1e-6) {
                val xEst = bezierX(s)
                if (xEst < x) min = s else max = s
                s = (min + max) / 2.0
            }
            return s
        }
    }
}
