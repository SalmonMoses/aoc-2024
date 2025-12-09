package me.salmonmoses.days.twentyfour

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import me.salmonmoses.geometry.BaseGrid
import me.salmonmoses.geometry.Grid
import me.salmonmoses.geometry.MutableGrid
import me.salmonmoses.geometry.Vector
import org.koin.core.annotation.Single

@Single
@Day(18, 2024)
class Day18 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
                "12\n" +
                        "5,4\n" +
                        "4,2\n" +
                        "4,5\n" +
                        "3,0\n" +
                        "2,1\n" +
                        "6,3\n" +
                        "2,4\n" +
                        "1,5\n" +
                        "0,6\n" +
                        "3,3\n" +
                        "2,6\n" +
                        "5,1\n" +
                        "1,2\n" +
                        "5,5\n" +
                        "2,5\n" +
                        "6,5\n" +
                        "1,4\n" +
                        "0,4\n" +
                        "6,4\n" +
                        "1,1\n" +
                        "6,1\n" +
                        "1,0\n" +
                        "0,5\n" +
                        "1,6\n" +
                        "2,0", "22"
        )
    override val spec2: TaskSpec
        get() = TaskSpec(
                "12\n" +
                        "5,4\n" +
                        "4,2\n" +
                        "4,5\n" +
                        "3,0\n" +
                        "2,1\n" +
                        "6,3\n" +
                        "2,4\n" +
                        "1,5\n" +
                        "0,6\n" +
                        "3,3\n" +
                        "2,6\n" +
                        "5,1\n" +
                        "1,2\n" +
                        "5,5\n" +
                        "2,5\n" +
                        "6,5\n" +
                        "1,4\n" +
                        "0,4\n" +
                        "6,4\n" +
                        "1,1\n" +
                        "6,1\n" +
                        "1,0\n" +
                        "0,5\n" +
                        "1,6\n" +
                        "2,0", "6,1"
        )

    private data class PathfindingInfo(val cameFrom: Vector?, val score: Int)

    private fun makeGrid(width: Int, height: Int, bytes: Set<Vector>): Grid<Boolean> =
            Grid(width, height) { it !in bytes }

    private fun findPath(grid: BaseGrid<Boolean>, start: Vector, end: Vector): MutableGrid<PathfindingInfo> {
        val infos = MutableGrid(grid.width, grid.height) { PathfindingInfo(null, Int.MAX_VALUE) }
        infos[0, 0] = PathfindingInfo(null, 0)
        val frontier = ArrayDeque<Vector>()
        frontier.add(start)
        while (frontier.isNotEmpty()) {
            val current = frontier.removeFirst()
            if (current == end) {
                break
            }

            val currentInfo = infos[current]

            grid.getNeighbors(current).forEach { neighbor ->
                if (grid[neighbor]) {
                    val newScore = currentInfo.score + 1
                    if (newScore < infos[neighbor].score) {
                        infos[neighbor] = PathfindingInfo(current, newScore)
                        frontier.add(neighbor)
                    }
                }
            }
        }
        return infos
    }

    private fun backtrackPath(grid: BaseGrid<Boolean>, start: Vector, end: Vector): Set<Vector> {
        val infos = findPath(grid, start, end)
        val path = mutableSetOf<Vector>()
        var currentNode: Vector? = end
        while (currentNode != null) {
            path.add(currentNode)
            currentNode = infos[currentNode].cameFrom
        }
        return path
    }

    override fun task1(input: List<String>, params: ParamsMap): String {
        val time = input[0].toInt()
        val bytes = input.drop(1).take(time).map {
            val pair = it.trim().split(",")
            val x = pair[0].toInt()
            val y = pair[1].toInt()
            Vector(x, y)
        }.toSet()
        val width = (bytes.maxOfOrNull(Vector::x) ?: 0) + 1
        val height = (bytes.maxOfOrNull(Vector::y) ?: 0) + 1
        val grid = makeGrid(width, height, bytes)
        val start = Vector(0, 0)
        val end = Vector(width - 1, height - 1)
        val path = findPath(grid, start, end)
        return path[end].score.toString()
    }

    override fun task2(input: List<String>, params: ParamsMap): String {
        val initialTime = input[0].toInt()
        val bytes = input.drop(1).map {
            val pair = it.trim().split(",")
            val x = pair[0].toInt()
            val y = pair[1].toInt()
            Vector(x, y)
        }
        val width = (bytes.maxOfOrNull(Vector::x) ?: 0) + 1
        val height = (bytes.maxOfOrNull(Vector::y) ?: 0) + 1
        val start = Vector(0, 0)
        val end = Vector(width - 1, height - 1)
        var grid = makeGrid(width, height, bytes.take(initialTime + 1).toSet())
        var path = backtrackPath(grid, start, end)
        for (t in (initialTime + 1)..<bytes.size) {
            if (bytes[t] in path) {
                grid = makeGrid(width, height, bytes.take(t + 1).toSet())
                path = backtrackPath(grid, start, end)
                if (start !in path || end !in path) {
                    return "${bytes[t].x},${bytes[t].y}"
                }
            }
        }
        return ""
    }
}