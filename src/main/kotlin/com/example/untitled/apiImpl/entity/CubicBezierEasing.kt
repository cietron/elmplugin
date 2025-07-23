package com.example.untitled.apiImpl.entity

import org.joml.Vector4d
import kotlin.math.abs

class CubicBezierEasing(
    private val vector: Vector4d

) {
    private val x1 = vector.x
    private val y1 = vector.y
    private val x2 = vector.z
    private val y2 = vector.w

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