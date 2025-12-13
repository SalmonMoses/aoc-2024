package me.salmonmoses.geometry

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

    fun intersects(other: Box): Boolean = edges.any {
        val s = it.firstCorner
        val e = it.secondCorner
        s.x == e.x && s.x in (other.minX + 1)..<other.maxX && maxOf(minOf(s.y, e.y), other.minY) < minOf(
                maxOf(s.y, e.y), other.maxY
        ) || s.y == e.y && s.y in (other.minY + 1)..<other.maxY && maxOf(
                minOf(s.x, e.x),
                other.minX
        ) < minOf(
                maxOf(s.x, e.x), other.maxX
        )
    }

}