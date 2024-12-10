package me.salmonmoses.utils

data class GridPoint(val x: Int, val y: Int) {
    operator fun plus(delta: GridPoint): GridPoint {
        return GridPoint(x + delta.x, y + delta.y)
    }

    operator fun minus(delta: GridPoint): GridPoint {
        return GridPoint(x - delta.x, y - delta.y)
    }

    operator fun times(k: Int): GridPoint {
        return GridPoint(x * k, y * k)
    }

    override operator fun equals(other: Any?): Boolean {
        return other is GridPoint && x == other.x && y == other.y
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    fun rotateClockwise(): GridPoint {
        return GridPoint(-y, x)
    }
}

abstract class BaseGrid<T> : Iterable<GridPoint> {
    abstract val height: Int
    abstract val width: Int

    abstract operator fun get(x: Int, y: Int): T
    open operator fun get(point: GridPoint): T = get(point.x, point.y)

    open fun isValid(x: Int, y: Int): Boolean {
        return x in 0..<width && y in 0..<height
    }

    fun isValid(point: GridPoint): Boolean {
        return isValid(point.x, point.y)
    }

    open fun getNeighbors(x: Int, y: Int): List<GridPoint> {
        val neighbors = mutableListOf<GridPoint>()
        if (isValid(x + 1, y)) {
            neighbors.add(GridPoint(x + 1, y))
        }
        if (isValid(x - 1, y)) {
            neighbors.add(GridPoint(x - 1, y))
        }
        if (isValid(x, y + 1)) {
            neighbors.add(GridPoint(x, y + 1))
        }
        if (isValid(x, y - 1)) {
            neighbors.add(GridPoint(x, y - 1))
        }
        return neighbors
    }

    fun getNeighbors(point: GridPoint): List<GridPoint> {
        return getNeighbors(point.x, point.y)
    }

    open fun getNeighborsDiagonal(x: Int, y: Int): List<GridPoint> {
        val neighbors = mutableListOf<GridPoint>()
        (-1..1).forEach { dy ->
            (-1..1).forEach { dx ->
                if (dx != 0 || dy != 0) {
                    val newX = x + dx
                    val newY = y + dy
                    if (isValid(newX, newY)) {
                        neighbors.add(GridPoint(newX, newY))
                    }
                }
            }
        }
        return neighbors
    }

    fun getNeighborsDiagonal(point: GridPoint): List<GridPoint> {
        return getNeighborsDiagonal(point.x, point.y)
    }

    override operator fun iterator(): Iterator<GridPoint> =
        (0..<height).cartesianProduct(0..<width).map { (x, y) -> GridPoint(x, y) }.iterator()
}

class Grid<T>(private val grid: List<List<T>>) : BaseGrid<T>() {
    override operator fun get(x: Int, y: Int): T = grid[y][x]

    override val height = grid.size
    override val width = grid[0].size

    fun getNeighborValues(x: Int, y: Int): List<T> = getNeighbors(x, y).map { this[it.x, it.y] }
    fun getNeighborValues(point: GridPoint): List<T> = getNeighbors(point).map { this[it.x, it.y] }

    fun getNeighborValuesDiagonal(x: Int, y: Int): List<T> =
        getNeighborsDiagonal(x, y).map { this[it.x, it.y] }

    fun getNeighborValuesDiagonal(point: GridPoint): List<T> =
        getNeighborsDiagonal(point).map { this[it.x, it.y] }
}

class MutableGrid<T>(private val grid: MutableList<MutableList<T>>) : BaseGrid<T>() {
    override operator fun get(x: Int, y: Int): T = grid[y][x]
    operator fun set(x: Int, y: Int, value: T) {
        grid[y][x] = value
    }

    operator fun set(point: GridPoint, value: T) {
        grid[point.y][point.x] = value
    }

    override val height = grid.size
    override val width = grid[0].size

    fun getNeighborValues(x: Int, y: Int): List<T> = getNeighbors(x, y).map { this[it.x, it.y] }
    fun getNeighborValues(point: GridPoint): List<T> = getNeighbors(point).map { this[it.x, it.y] }

    fun getNeighborValuesDiagonal(x: Int, y: Int): List<T> =
        getNeighborsDiagonal(x, y).map { this[it.x, it.y] }

    fun getNeighborValuesDiagonal(point: GridPoint): List<T> =
        getNeighborsDiagonal(point).map { this[it.x, it.y] }
}

class VirtualGrid<T>(
    private val minWidth: Int,
    private val maxWidth: Int,
    private val minHeight: Int,
    private val maxHeight: Int,
    private val elements: Map<GridPoint, T>
) : BaseGrid<T?>() {
    override val width: Int
        get() = maxWidth - minWidth
    override val height: Int
        get() = maxHeight - minHeight

    override fun get(x: Int, y: Int): T? = elements[GridPoint(x, y)]
    override fun get(point: GridPoint): T? = elements[point]
    override fun isValid(x: Int, y: Int): Boolean = x in minWidth..maxWidth && y in minHeight..maxHeight

    operator fun contains(point: GridPoint): Boolean = point in elements
}