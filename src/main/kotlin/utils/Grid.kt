package me.salmonmoses.utils

import kotlin.math.pow

data class Vector(val x: Int, val y: Int) {
    operator fun plus(delta: Vector): Vector {
        return Vector(x + delta.x, y + delta.y)
    }

    operator fun minus(delta: Vector): Vector {
        return Vector(x - delta.x, y - delta.y)
    }

    operator fun times(k: Int): Vector {
        return Vector(x * k, y * k)
    }

    override operator fun equals(other: Any?): Boolean {
        return other is Vector && x == other.x && y == other.y
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    fun rotateClockwise(): Vector {
        return Vector(-y, x)
    }

    fun length(): Double {
        return x.toDouble().pow(2.0) + y.toDouble().pow(2.0)
    }
}

abstract class BaseGrid<T> : Iterable<Vector> {
    abstract val height: Int
    abstract val width: Int

    abstract operator fun get(x: Int, y: Int): T
    open operator fun get(point: Vector): T = get(point.x, point.y)

    open fun isValid(x: Int, y: Int): Boolean {
        return x in 0..<width && y in 0..<height
    }

    fun isValid(point: Vector): Boolean {
        return isValid(point.x, point.y)
    }

    open fun getNeighbors(x: Int, y: Int): List<Vector> {
        val neighbors = mutableListOf<Vector>()
        if (isValid(x + 1, y)) {
            neighbors.add(Vector(x + 1, y))
        }
        if (isValid(x - 1, y)) {
            neighbors.add(Vector(x - 1, y))
        }
        if (isValid(x, y + 1)) {
            neighbors.add(Vector(x, y + 1))
        }
        if (isValid(x, y - 1)) {
            neighbors.add(Vector(x, y - 1))
        }
        return neighbors
    }

    fun getNeighbors(point: Vector): List<Vector> {
        return getNeighbors(point.x, point.y)
    }

    open fun getNeighborsDiagonal(x: Int, y: Int): List<Vector> {
        val neighbors = mutableListOf<Vector>()
        (-1..1).forEach { dy ->
            (-1..1).forEach { dx ->
                if (dx != 0 || dy != 0) {
                    val newX = x + dx
                    val newY = y + dy
                    if (isValid(newX, newY)) {
                        neighbors.add(Vector(newX, newY))
                    }
                }
            }
        }
        return neighbors
    }

    fun getNeighborsDiagonal(point: Vector): List<Vector> {
        return getNeighborsDiagonal(point.x, point.y)
    }

    override operator fun iterator(): Iterator<Vector> =
        (0..<height).cartesianProduct(0..<width).map { (x, y) -> Vector(x, y) }.iterator()
}

class Grid<T>(private val grid: List<List<T>>) : BaseGrid<T>() {
    override operator fun get(x: Int, y: Int): T = grid[y][x]

    override val height = grid.size
    override val width = grid[0].size

    fun getNeighborValues(x: Int, y: Int): List<T> = getNeighbors(x, y).map { this[it.x, it.y] }
    fun getNeighborValues(point: Vector): List<T> = getNeighbors(point).map { this[it.x, it.y] }

    fun getNeighborValuesDiagonal(x: Int, y: Int): List<T> =
        getNeighborsDiagonal(x, y).map { this[it.x, it.y] }

    fun getNeighborValuesDiagonal(point: Vector): List<T> =
        getNeighborsDiagonal(point).map { this[it.x, it.y] }
}

class MutableGrid<T>(private val grid: MutableList<MutableList<T>>) : BaseGrid<T>() {
    override operator fun get(x: Int, y: Int): T = grid[y][x]
    operator fun set(x: Int, y: Int, value: T) {
        grid[y][x] = value
    }

    operator fun set(point: Vector, value: T) {
        grid[point.y][point.x] = value
    }

    override val height = grid.size
    override val width = grid[0].size

    fun getNeighborValues(x: Int, y: Int): List<T> = getNeighbors(x, y).map { this[it.x, it.y] }
    fun getNeighborValues(point: Vector): List<T> = getNeighbors(point).map { this[it.x, it.y] }

    fun getNeighborValuesDiagonal(x: Int, y: Int): List<T> =
        getNeighborsDiagonal(x, y).map { this[it.x, it.y] }

    fun getNeighborValuesDiagonal(point: Vector): List<T> =
        getNeighborsDiagonal(point).map { this[it.x, it.y] }
}

class VirtualGrid<T>(
    private val minWidth: Int,
    private val maxWidth: Int,
    private val minHeight: Int,
    private val maxHeight: Int,
    private val elements: Map<Vector, T>
) : BaseGrid<T?>() {
    override val width: Int
        get() = maxWidth - minWidth
    override val height: Int
        get() = maxHeight - minHeight

    override fun get(x: Int, y: Int): T? = elements[Vector(x, y)]
    override fun get(point: Vector): T? = elements[point]
    override fun isValid(x: Int, y: Int): Boolean = x in minWidth..maxWidth && y in minHeight..maxHeight

    operator fun contains(point: Vector): Boolean = point in elements
}