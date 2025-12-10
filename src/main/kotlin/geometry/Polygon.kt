package me.salmonmoses.geometry

import me.salmonmoses.utils.rangesOverlap

class Polygon(val vertices: List<Vector>) {
    val edges: List<Box>

    init {
        val nextEdges = vertices.windowed(2)
                .map { Box(it[0], it[1]) }
                .toMutableList()
        nextEdges.add(Box(vertices.first(), vertices.last()))
        edges = nextEdges.toList()
    }

    operator fun contains(point: Vector): Boolean {
        val intersections = edges
                .count {
                    it.isVerticalLine &&
                            it.minX >= point.x &&
                            point.y in it.minY..it.maxY
                }
        return intersections % 2 == 1 || (point in vertices && intersections % 2 == 0)
    }

    fun intersects(other: Box): Boolean = edges.any { e ->
        if (e.isHorizontalLine && other.isHorizontalLine) {
            e.minY == other.minY && rangesOverlap(e.minX..e.maxX, other.minX..other.maxX)
        } else if (e.isVerticalLine && other.isVerticalLine) {
            e.minX == other.minX && rangesOverlap(e.minY..e.maxY, other.minY..other.maxY)
        } else if (e.isVerticalLine && other.isHorizontalLine) {
            e.minX in other.minX..other.maxX && other.minY in e.minY..e.maxY
        } else if (e.isHorizontalLine && other.isVerticalLine) {
            other.minX in e.minX..e.maxX && e.minY in other.minY..other.maxY
        } else {
            TODO()
        }
    }

    fun includes(other: Box): Boolean =
            other.topLeft in this
                    && other.topRight in this
                    && other.bottomLeft in this
                    && other.bottomRight in this
                    && other.topRight.midpoint(other.topLeft) in this
                    && other.bottomRight.midpoint(other.topRight) in this
                    && other.bottomLeft.midpoint(other.topLeft) in this
                    && other.bottomRight.midpoint(other.bottomLeft) in this
//                    && !intersects(Box(other.topLeft, other.topRight))
//                    && !intersects(Box(other.topRight, other.bottomRight))
//                    && !intersects(Box(other.bottomRight, other.bottomLeft))
//                    && !intersects(Box(other.bottomLeft, other.topLeft))
}