package me.salmonmoses.utils

import kotlin.math.absoluteValue
import kotlin.math.max
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

    fun sqrtLength(): Double {
        return x.toDouble().pow(2.0) + y.toDouble().pow(2.0)
    }

    fun manhattan(other: Vector): Int {
        return (x - other.x).absoluteValue + (y - other.y).absoluteValue
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

open class Grid<T>(final override val width: Int, final override val height: Int, init: (Vector) -> T) : BaseGrid<T>() {
    protected val grid = ArrayList<T>(width * height)

    init {
        (0..<height).forEach { y ->
            (0..<width).forEach { x -> grid.add(init(Vector(x, y))) }
        }
    }

    override operator fun get(x: Int, y: Int): T = grid[y * width + x]

    constructor(gridInit: List<List<T>>) : this(gridInit[0].size, gridInit.size, { gridInit[it.y][it.x] })

    fun getNeighborValues(x: Int, y: Int): List<T> = getNeighbors(x, y).map { this[it.x, it.y] }
    fun getNeighborValues(point: Vector): List<T> = getNeighbors(point).map { this[it.x, it.y] }

    fun getNeighborValuesDiagonal(x: Int, y: Int): List<T> =
        getNeighborsDiagonal(x, y).map { this[it.x, it.y] }

    fun getNeighborValuesDiagonal(point: Vector): List<T> =
        getNeighborsDiagonal(point).map { this[it.x, it.y] }
}

class MutableGrid<T>(width: Int, height: Int, init: (Vector) -> T) : Grid<T>(width, height, init) {
    constructor(gridInit: List<List<T>>) : this(gridInit[0].size, gridInit.size, { gridInit[it.y][it.x] })

    operator fun set(x: Int, y: Int, value: T) {
        grid[y * width + x] = value
    }

    operator fun set(point: Vector, value: T) {
        grid[point.y * width + point.x] = value
    }

    fun copy(): MutableGrid<T> = MutableGrid(width, height) { this[it.x, it.y] }

    fun copyWith(point: Vector, overridenValue: T): MutableGrid<T> {
        val copiedGrid = MutableGrid(width, height, { this[it.x, it.y] })
        copiedGrid[point] = overridenValue
        return copiedGrid
    }
}

open class VirtualGrid<T>(
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

class MutableVirtualGrid<T>(
    minWidth: Int,
    maxWidth: Int,
    minHeight: Int,
    maxHeight: Int,
    private val elements: MutableMap<Vector, T>
) : VirtualGrid<T>(minWidth, minHeight, maxWidth, maxHeight, elements) {
    operator fun set(x: Int, y: Int, value: T) {
        elements[Vector(x, y)] = value
    }

    operator fun set(point: Vector, value: T) {
        elements[point] = value
    }

    fun remove(x: Int, y: Int) {
        elements.remove(Vector(x, y))
    }

    fun remove(point: Vector) {
        elements.remove(point)
    }
}