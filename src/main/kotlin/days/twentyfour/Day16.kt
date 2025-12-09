package me.salmonmoses.days.twentyfour

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import me.salmonmoses.geometry.Grid
import me.salmonmoses.geometry.Vector
import org.koin.core.annotation.Single
import java.util.*

@Single
@Day(16, 2024)
class Day16 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
                "###############\n" +
                        "#.......#....E#\n" +
                        "#.#.###.#.###.#\n" +
                        "#.....#.#...#.#\n" +
                        "#.###.#####.#.#\n" +
                        "#.#.#.......#.#\n" +
                        "#.#.#####.###.#\n" +
                        "#...........#.#\n" +
                        "###.#.#####.#.#\n" +
                        "#...#.....#.#.#\n" +
                        "#.#.#.###.#.#.#\n" +
                        "#.....#...#.#.#\n" +
                        "#.###.#.#.#.#.#\n" +
                        "#S..#.....#...#\n" +
                        "###############", "7036"
        )
    override val spec2: TaskSpec
        get() = TaskSpec(
                "###############\n" +
                        "#.......#....E#\n" +
                        "#.#.###.#.###.#\n" +
                        "#.....#.#...#.#\n" +
                        "#.###.#####.#.#\n" +
                        "#.#.#.......#.#\n" +
                        "#.#.#####.###.#\n" +
                        "#...........#.#\n" +
                        "###.#.#####.#.#\n" +
                        "#...#.....#.#.#\n" +
                        "#.#.#.###.#.#.#\n" +
                        "#.....#...#.#.#\n" +
                        "#.###.#.#.#.#.#\n" +
                        "#S..#.....#...#\n" +
                        "###############", "45"
        )

    private data class PathfindingInfo(val cameFrom: Vector?, val direction: Vector, val score: Int)

    override fun task1(input: List<String>, params: ParamsMap): String {
        var start = Vector(0, 0)
        var end = Vector(0, 0)
        val grid = Grid(input.mapIndexed { y, row ->
            row.mapIndexed { x, cell ->
                when (cell) {
                    'S' -> {
                        start = Vector(x, y)
                        false
                    }

                    'E' -> {
                        end = Vector(x, y)
                        false
                    }

                    '#' -> true

                    else -> false
                }
            }
        })
        val infos = mutableMapOf(
                start to PathfindingInfo(null, Vector(1, 0), 0),
        )
        val frontier = PriorityQueue<Pair<Vector, Int>> { first, second -> first.second - second.second }
        frontier.offer(start to 0)
        while (frontier.isNotEmpty()) {
            val current = frontier.poll().first
            if (current == end) {
                continue
            }

            val currentInfo = infos[current]!!

            grid.getNeighbors(current).forEach { neighbor ->
                if (!grid[neighbor]) {
                    val direction = neighbor - current
                    val newScore =
                            currentInfo.score + (if (direction == currentInfo.direction) 1 else 1001)
                    if (neighbor !in infos || newScore < infos[neighbor]!!.score) {
                        infos[neighbor] = PathfindingInfo(current, direction, newScore)
                        frontier.offer(neighbor to end.manhattan(neighbor))
                    }
                }
            }
        }
        return infos[end]!!.score.toString()
    }

    override fun task2(input: List<String>, params: ParamsMap): String {
        var start = Vector(0, 0)
        var end = Vector(0, 0)
        val grid = Grid(input.mapIndexed { y, row ->
            row.mapIndexed { x, cell ->
                when (cell) {
                    'S' -> {
                        start = Vector(x, y)
                        false
                    }

                    'E' -> {
                        end = Vector(x, y)
                        false
                    }

                    '#' -> true

                    else -> false
                }
            }
        })
        val infos = mutableMapOf(
                start to PathfindingInfo(null, Vector(1, 0), 0),
        )
        val frontier = PriorityQueue<Pair<Vector, Int>> { first, second -> first.second - second.second }
        frontier.offer(start to 0)
        while (frontier.isNotEmpty()) {
            val current = frontier.poll().first
            if (current == end) {
                continue
            }

            val currentInfo = infos[current]!!

            grid.getNeighbors(current).forEach { neighbor ->
                if (!grid[neighbor]) {
                    val direction = neighbor - current
                    val newScore =
                            currentInfo.score + (if (direction == currentInfo.direction) 1 else 1001)
                    if (neighbor !in infos || newScore < infos[neighbor]!!.score) {
                        infos[neighbor] = PathfindingInfo(current, direction, newScore)
                        frontier.add(neighbor to end.manhattan(neighbor))
                    }
                }
            }
        }
        val path = mutableSetOf<Vector>()
        var currentNode: Vector? = end
        while (currentNode != null) {
            path.add(currentNode)
            currentNode = infos[currentNode]?.cameFrom
        }
        return path.size.toString()
    }
}