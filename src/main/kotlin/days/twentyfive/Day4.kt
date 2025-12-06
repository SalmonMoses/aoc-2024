package me.salmonmoses.days.twentyfive

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.TaskSpec
import me.salmonmoses.utils.Grid
import me.salmonmoses.utils.MutableGrid
import me.salmonmoses.utils.Predicates
import org.koin.core.annotation.Single
import java.util.function.Predicate

@Single
@Day(4, 2025)
class Day4 : DayTask {
    override val spec1: TaskSpec?
        get() = TaskSpec(
            "..@@.@@@@.\n" +
                    "@@@.@.@.@@\n" +
                    "@@@@@.@.@@\n" +
                    "@.@@@@..@.\n" +
                    "@@.@@@@.@@\n" +
                    ".@@@@@@@.@\n" +
                    ".@.@.@.@@@\n" +
                    "@.@@@.@@@@\n" +
                    ".@@@@@@@@.\n" +
                    "@.@.@@@.@.", "13"
        )
    override val spec2: TaskSpec?
        get() = TaskSpec(
            "..@@.@@@@.\n" +
                    "@@@.@.@.@@\n" +
                    "@@@@@.@.@@\n" +
                    "@.@@@@..@.\n" +
                    "@@.@@@@.@@\n" +
                    ".@@@@@@@.@\n" +
                    ".@.@.@.@@@\n" +
                    "@.@@@.@@@@\n" +
                    ".@@@@@@@@.\n" +
                    "@.@.@@@.@.", "43"
        )

    override fun task1(input: List<String>): String {
        val grid = Grid(input.map { row -> row.map { it == '@' } })
        return grid
            .count { point -> grid[point] && grid.getNeighborValuesDiagonal(point).count { it } < 4 }
            .toString()
    }

    override fun task2(input: List<String>): String {
        val grid = MutableGrid(input.map { row -> row.map { it == '@' } })
        var shouldContinue: Boolean
        var result = 0
        do {
            shouldContinue = false
            grid.filter { grid[it] }.forEach { point ->
                if (grid.getNeighborValuesDiagonal(point).count { it } < 4) {
                    result++
                    grid[point] = false
                    shouldContinue = true
                }
            }
        } while (shouldContinue)
        return result.toString()
    }
}