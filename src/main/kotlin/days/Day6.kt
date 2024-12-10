package me.salmonmoses.days

import me.salmonmoses.utils.*
import org.koin.core.annotation.Single

@Single
@Day(6)
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
    override val spec2: TaskSpec?
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
        startingPosition: GridPoint,
        startingDirection: GridPoint
    ): Boolean {
        var guardPosition = startingPosition
        var guardDirection = startingDirection
        val visited = mutableSetOf<Pair<GridPoint, GridPoint>>()
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

    override fun task1(input: List<String>): String {
        var guardPosition = GridPoint(0, 0)
        var guardDirection = GridPoint(0, -1)
        val gridLists = mutableListOf<List<Boolean>>()
        input.indices.forEach { y ->
            val rowList = mutableListOf<Boolean>()
            input[y].trim().indices.forEach { x ->
                when (input[y][x]) {
                    '.' -> rowList.add(true)
                    '#' -> rowList.add(false)
                    '^' -> {
                        guardPosition = GridPoint(x, y)
                        rowList.add(true)
                    }
                }
            }
            gridLists.add(rowList)
        }
        val grid = Grid(gridLists)
        val visited = mutableSetOf<GridPoint>()
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

    override fun task2(input: List<String>): String {
        var guardPosition = GridPoint(0, 0)
        val guardDirection = GridPoint(0, -1)
        val gridLists = mutableListOf<MutableList<Boolean>>()
        input.indices.forEach { y ->
            val rowList = mutableListOf<Boolean>()
            input[y].trim().indices.forEach { x ->
                when (input[y][x]) {
                    '.' -> rowList.add(true)
                    '#' -> rowList.add(false)
                    '^' -> {
                        guardPosition = GridPoint(x, y)
                        rowList.add(true)
                    }
                }
            }
            gridLists.add(rowList)
        }
        val grid = MutableGrid(gridLists)
        val potentialObstructions = mutableSetOf<GridPoint>()
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