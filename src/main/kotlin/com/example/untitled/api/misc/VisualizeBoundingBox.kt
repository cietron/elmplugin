package com.example.untitled.api.misc

import org.joml.Vector3d

interface VisualizeBoundingBox {
    fun with(p1: Vector3d, p2: Vector3d, seconds: Long)
}