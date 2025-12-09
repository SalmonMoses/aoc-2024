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

    fun intersects(other: Box): Boolean = edges.firstOrNull {
        it.aabb(other)
    } != null
}