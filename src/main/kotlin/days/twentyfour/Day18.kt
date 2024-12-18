package me.salmonmoses.days.twentyfour

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.TaskSpec
import me.salmonmoses.utils.*
import org.koin.core.annotation.Single
import java.util.PriorityQueue
import kotlin.collections.ArrayDeque

@Single
@Day(18)
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
    override val spec2: TaskSpec?
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

    private data class DijkstraInfo(val cameFrom: Vector?, val score: Int)

    private fun findPath(grid: VirtualGrid<Boolean>, start: Vector, end: Vector): Map<Vector, DijkstraInfo> {
        val infos = mutableMapOf(
            start to DijkstraInfo(null, 0),
        )
        val frontier = ArrayDeque<Vector>()
        frontier.add(start)
        for (current in DequeIterator(frontier)) {
            if (current == end) {
                break
            }

            val currentInfo = infos[current]!!

            grid.getNeighbors(current).forEach { neighbor ->
                if (neighbor !in grid) {
                    val newScore = currentInfo.score + 1
                    if (neighbor !in infos || newScore < infos[neighbor]!!.score) {
                        infos[neighbor] = DijkstraInfo(current, newScore)
                        frontier.add(neighbor)
                    }
                }
            }
        }
        return infos
    }

    private fun backtrackPath(grid: VirtualGrid<Boolean>, start: Vector, end: Vector): Set<Vector> {
        val infos = findPath(grid, start, end)
        if (end !in infos) {
            return emptySet()
        }
        val path = mutableSetOf<Vector>()
        var currentNode: Vector? = end
        while (currentNode != null) {
            path.add(currentNode)
            currentNode = infos[currentNode]?.cameFrom
        }
        return path
    }

    override fun task1(input: List<String>): String {
        val time = input[0].toInt()
        val bytes = input.drop(1).take(time).associate {
            val pair = it.trim().split(",")
            val x = pair[0].toInt()
            val y = pair[1].toInt()
            Vector(x, y) to true
        }
        val width = (bytes.keys.maxOfOrNull(Vector::x) ?: 0)
        val height = (bytes.keys.maxOfOrNull(Vector::y) ?: 0)
        val grid = VirtualGrid(0, width, 0, height, bytes)
        val start = Vector(0, 0)
        val end = Vector(width, height)
        val path = findPath(grid, start, end)
        return path[end]?.score?.toString() ?: ""
    }

    override fun task2(input: List<String>): String {
        val initialTime = input[0].toInt()
        val bytes = input.drop(1).map {
            val pair = it.trim().split(",")
            val x = pair[0].toInt()
            val y = pair[1].toInt()
            Vector(x, y)
        }
        val width = (bytes.maxOfOrNull(Vector::x) ?: 0)
        val height = (bytes.maxOfOrNull(Vector::y) ?: 0)
        val start = Vector(0, 0)
        val end = Vector(width, height)
        var grid = VirtualGrid(0, width, 0, height, bytes.take(initialTime + 1).associateWith { true })
        var path = backtrackPath(grid, start, end)
        for (t in (initialTime + 1)..<bytes.size) {
            if (bytes[t] in path) {
                grid = VirtualGrid(0, width, 0, height, bytes.take(t + 1).associateWith { true })
                path = backtrackPath(grid, start, end)
                if (end !in path) {
                    return "${bytes[t].x},${bytes[t].y}"
                }
            }
        }
        return ""
    }
}