package com.example.untitled.api.capture

import com.example.untitled.api.entity.SelectableEntity
import org.joml.Vector3d

interface Rectangle {
    fun capture(p1: Vector3d, p2: Vector3d): Collection<SelectableEntity>
}