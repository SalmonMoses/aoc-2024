package me.salmonmoses.days.twentyfour

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import me.salmonmoses.geometry.BaseGrid
import me.salmonmoses.geometry.MutableGrid
import me.salmonmoses.geometry.Vector
import org.koin.core.annotation.Single

@Single
@Day(6, 2024)
class Day6 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
                "....#.....\n" +
                        ".........#\n" +
                        "..........\n" +
                        "..#.......\n" +
                        ".......#..\n" +
                        "..........\n" +
                        ".#..^.....\n" +
                        "........#.\n" +
                        "#.........\n" +
                        "......#...", "41"
        )
    override val spec2: TaskSpec
        get() = TaskSpec(
                "....#.....\n" +
                        ".........#\n" +
                        "..........\n" +
                        "..#.......\n" +
                        ".......#..\n" +
                        "..........\n" +
                        ".#..^.....\n" +
                        "........#.\n" +
                        "#.........\n" +
                        "......#...", "6"
        )

    private fun traceGuard(
            grid: BaseGrid<Boolean>,
            startingPosition: Vector,
            startingDirection: Vector
    ): Boolean {
        var guardPosition = startingPosition
        var guardDirection = startingDirection
        val visited = mutableSetOf<Pair<Vector, Vector>>()
        while (grid.isValid(guardPosition)) {
            val positionDirection = Pair(guardPosition, guardDirection)
            if (positionDirection in visited) return false
            visited.add(positionDirection)
            val nextPosition = guardPosition + guardDirection
            if (grid.isValid(nextPosition) && !grid[nextPosition]) {
                guardDirection = guardDirection.rotateClockwise()
            } else {
                guardPosition = nextPosition
            }
        }
        return true
    }

    override fun task1(input: List<String>, params: ParamsMap): String {
        var guardPosition = Vector(0, 0)
        var guardDirection = Vector(0, -1)
        val grid = MutableGrid(input.size, input[0].trim().length) {
            when (input[it.y][it.x]) {
                '.' -> true
                '#' -> false
                '^' -> {
                    guardPosition = it
                    true
                }

                else -> true
            }
        }
        val visited = mutableSetOf<Vector>()
        while (grid.isValid(guardPosition)) {
            visited.add(guardPosition)
            val nextPosition = guardPosition + guardDirection
            if (grid.isValid(nextPosition) && !grid[nextPosition]) {
                guardDirection = guardDirection.rotateClockwise()
            } else {
                guardPosition = nextPosition
            }
        }
        return visited.size.toString()
    }

    override fun task2(input: List<String>, params: ParamsMap): String {
        var guardPosition = Vector(0, 0)
        val guardDirection = Vector(0, -1)
        val grid = MutableGrid(input.size, input[0].trim().length) {
            when (input[it.y][it.x]) {
                '.' -> true
                '#' -> false
                '^' -> {
                    guardPosition = it
                    true
                }

                else -> true
            }
        }
        val potentialObstructions = mutableSetOf<Vector>()
        for (potentialObstruction in grid) {
            if (!grid[potentialObstruction]) {
                continue
            }
            grid[potentialObstruction] = false
            if (!traceGuard(grid, guardPosition, guardDirection)) {
                potentialObstructions.add(potentialObstruction)
            }
            grid[potentialObstruction] = true
        }
        return potentialObstructions.size.toString()
    }
}