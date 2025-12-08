package me.salmonmoses.days.twentyfour

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import me.salmonmoses.utils.Grid
import me.salmonmoses.utils.Vector
import org.koin.core.annotation.Single

@Single
@Day(4, 2024)
class Day4 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
            "MMMSXXMASM\n" +
                    "MSAMXMSMSA\n" +
                    "AMXSXMAAMM\n" +
                    "MSAMASMSMX\n" +
                    "XMASAMXAMM\n" +
                    "XXAMMXXAMA\n" +
                    "SMSMSASXSS\n" +
                    "SAXAMASAAA\n" +
                    "MAMMMXMMMM\n" +
                    "MXMXAXMASX", "18"
        )
    override val spec2: TaskSpec?
        get() = TaskSpec(
            "MMMSXXMASM\n" +
                    "MSAMXMSMSA\n" +
                    "AMXSXMAAMM\n" +
                    "MSAMASMSMX\n" +
                    "XMASAMXAMM\n" +
                    "XXAMMXXAMA\n" +
                    "SMSMSASXSS\n" +
                    "SAXAMASAAA\n" +
                    "MAMMMXMMMM\n" +
                    "MXMXAXMASX", "9"
        )

    override fun task1(input: List<String>, params: ParamsMap): String {
        val grid = Grid(input.map { it.split("").filter(String::isNotBlank) })
        val xs = grid.filter { grid[it] == "X" }

        var xmasCount = 0
        for (potentialStart in xs) {
            for (neighbor in grid.getNeighborsDiagonal(potentialStart)
                .filter { grid[it] == "M" }) {
                val direction = neighbor - potentialStart
                val potentialA = neighbor + direction
                val potentialS = neighbor + direction * 2
                if (grid.isValid(potentialA) && grid[potentialA] == "A"
                    && grid.isValid(potentialS) && grid[potentialS] == "S"
                ) {
                    xmasCount++
                }
            }
        }

        return xmasCount.toString()
    }

    override fun task2(input: List<String>, params: ParamsMap): String {
        val grid = Grid(input.map { it.split("").filter(String::isNotBlank) })
        val starts = grid.filter { it.x in 1..<(grid.width - 1) && it.y in 1..<(grid.height - 1) && grid[it] == "A" }

        val rightDiagonal = Vector(1, 1)
        val leftDiagonal = Vector(-1, 1)
        var xmasCount = 0
        for (point in starts) {
            if (((grid[point + rightDiagonal] == "M" && grid[point - rightDiagonal] == "S")
                        || (grid[point + rightDiagonal] == "S" && grid[point - rightDiagonal] == "M"))
                && ((grid[point + leftDiagonal] == "M" && grid[point - leftDiagonal] == "S")
                        || (grid[point + leftDiagonal] == "S" && grid[point - leftDiagonal] == "M"))
            ) {
                xmasCount++
            }
        }

        return xmasCount.toString()
    }
}