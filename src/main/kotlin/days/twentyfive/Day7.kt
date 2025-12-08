package me.salmonmoses.days.twentyfive

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import me.salmonmoses.utils.Grid
import me.salmonmoses.utils.MutableGrid
import me.salmonmoses.utils.Vector
import org.koin.core.annotation.Single

@Single
@Day(7, 2025)
class Day7 : DayTask {
    private enum class GridObject(val debugChar: Char) {
        Splitter('^'),
        Start('S'),
        Beam('|'),
        Empty('.')
    }

    override val spec1: TaskSpec
        get() = TaskSpec(
                ".......S.......\n" +
                        "...............\n" +
                        ".......^.......\n" +
                        "...............\n" +
                        "......^.^......\n" +
                        "...............\n" +
                        ".....^.^.^.....\n" +
                        "...............\n" +
                        "....^.^...^....\n" +
                        "...............\n" +
                        "...^.^...^.^...\n" +
                        "...............\n" +
                        "..^...^.....^..\n" +
                        "...............\n" +
                        ".^.^.^.^.^...^.\n" +
                        "...............", "21"
        )
    override val spec2: TaskSpec
        get() = TaskSpec(
                ".......S.......\n" +
                        "...............\n" +
                        ".......^.......\n" +
                        "...............\n" +
                        "......^.^......\n" +
                        "...............\n" +
                        ".....^.^.^.....\n" +
                        "...............\n" +
                        "....^.^...^....\n" +
                        "...............\n" +
                        "...^.^...^.^...\n" +
                        "...............\n" +
                        "..^...^.....^..\n" +
                        "...............\n" +
                        ".^.^.^.^.^...^.\n" +
                        "...............", "40"
        )

    override fun task1(input: List<String>, params: ParamsMap): String {
        val grid = MutableGrid(input.map { row ->
            row.map {
                when (it) {
                    '.' -> GridObject.Empty
                    'S' -> GridObject.Start
                    '^' -> GridObject.Splitter
                    else -> GridObject.Empty
                }
            }.toList()
        }.toList())

        var splits = 0
        for (y in 1..<grid.height) {
            val prevY = y - 1
            for (x in 0..<grid.width) {
                val currentObject = grid[x, y]
                val objectAbove = grid[x, prevY]
                when (currentObject) {
                    GridObject.Splitter -> {
                        if (objectAbove == GridObject.Beam) {
                            grid[x - 1, y] = GridObject.Beam
                            grid[x + 1, y] = GridObject.Beam
                            splits++
                        }
                    }

                    GridObject.Empty -> {
                        if (objectAbove == GridObject.Beam || objectAbove == GridObject.Start) {
                            grid[x, y] = GridObject.Beam
                        }
                    }

                    else -> {}
                }
            }
        }

        return splits.toString()
    }

    private fun traceTimeline(
            grid: Grid<GridObject>,
            start: Vector,
            cachedTimelines: MutableMap<Vector, Long>
    ): Long {
        if (cachedTimelines.containsKey(start)) {
            return cachedTimelines[start]!!
        }

        if (!grid.isValid(start)) {
            return 0L
        }

        var timelines = 1L
        var y = start.y
        while (grid[start.x, y] != GridObject.Splitter && y < (grid.height - 1)) {
            y++
        }

        timelines += traceTimeline(grid, Vector(start.x - 1, y + 1), cachedTimelines)
        timelines += traceTimeline(grid, Vector(start.x + 1, y + 1), cachedTimelines)

        cachedTimelines[start] = timelines
        return timelines
    }

    override fun task2(input: List<String>, params: ParamsMap): String {
        val grid = Grid(input.map { row ->
            row.map {
                when (it) {
                    '.' -> GridObject.Empty
                    'S' -> GridObject.Start
                    '^' -> GridObject.Splitter
                    else -> GridObject.Empty
                }
            }.toList()
        }.toList())

        for (x in 0..<grid.width) {
            if (grid[x, 0] == GridObject.Start) {
                return ((traceTimeline(
                        grid,
                        Vector(x, 1),
                        mutableMapOf()
                ) + 1) / 2).toString()
            }
        }

        return ""
    }
}