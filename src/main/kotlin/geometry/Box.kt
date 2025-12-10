package me.salmonmoses.geometry

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Box(val firstCorner: Vector, val secondCorner: Vector) {
    val width = abs(firstCorner.x - secondCorner.x) + 1
    val height = abs(firstCorner.y - secondCorner.y) + 1

    val minX = min(firstCorner.x, secondCorner.x)
    val minY = min(firstCorner.y, secondCorner.y)
    val maxX = max(firstCorner.x, secondCorner.x)
    val maxY = max(firstCorner.y, secondCorner.y)

    val topLeft = Vector(minX, minY)
    val topRight = Vector(maxX, minY)
    val bottomLeft = Vector(minX, maxY)
    val bottomRight = Vector(maxX, maxY)

    val isVerticalLine = minX == maxX
    val isHorizontalLine = minY == maxY

    constructor(corners: Pair<Vector, Vector>) : this(corners.first, corners.second)

    val area
        get() = if (isHorizontalLine) {
            width.toLong()
        } else if (isVerticalLine) {
            height.toLong()
        } else {
            width.toLong() * height.toLong()
        }
}