package me.salmonmoses.days

import me.salmonmoses.utils.DequeIterator
import me.salmonmoses.utils.Grid
import me.salmonmoses.utils.GridPoint
import org.koin.core.annotation.Single

@Single
@Day(10)
class Day10 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
            "89010123\n" +
                    "78121874\n" +
                    "87430965\n" +
                    "96549874\n" +
                    "45678903\n" +
                    "32019012\n" +
                    "01329801\n" +
                    "10456732", "36"
        )
    override val spec2: TaskSpec?
        get() = TaskSpec(
            "89010123\n" +
                    "78121874\n" +
                    "87430965\n" +
                    "96549874\n" +
                    "45678903\n" +
                    "32019012\n" +
                    "01329801\n" +
                    "10456732", "81"
        )

    private fun traceTrailToNine(start: GridPoint, grid: Grid<Int>): Int {
        if (!grid.isValid(start) || grid[start] != 0) {
            return 0
        }

        var visitedNines = 0
        val frontier = ArrayDeque<GridPoint>()
        val visited = mutableSetOf<GridPoint>()
        frontier.addLast(start)
        for (current in DequeIterator(frontier)) {
            if (current in visited) {
                continue
            }
            visited.add(current)

            if (grid[current] == 9) {
                visitedNines++
            } else {
                val neighbors = grid.getNeighbors(current)
                neighbors.filter { it !in visited && grid[it] - grid[current] == 1 }.forEach { frontier.addLast(it) }
            }
        }
        return visitedNines
    }

    private fun traceTrailNumber(start: GridPoint, grid: Grid<Int>): Int {
        if (!grid.isValid(start) || grid[start] != 0) {
            return 0
        }

        var trails = 1
        val frontier = ArrayDeque<GridPoint>()
        frontier.addLast(start)
        for (current in DequeIterator(frontier)) {
            if (grid[current] != 9) {
                val neighbors = grid.getNeighbors(current)
                val nextSlopes = neighbors.filter { grid[it] - grid[current] == 1 }
                trails += nextSlopes.size - 1
                nextSlopes.forEach { frontier.addLast(it) }
            }
        }
        return trails
    }

    override fun task1(input: List<String>): String {
        val gridList = input.map { row -> row.split("").filterNot { it.isBlank() }.map(String::toInt) }
        val grid = Grid(gridList)
        return grid.filter { grid[it] == 0 }.sumOf { traceTrailToNine(it, grid) }.toString()
    }

    override fun task2(input: List<String>): String {
        val gridList = input.map { row -> row.split("").filterNot { it.isBlank() }.map(String::toInt) }
        val grid = Grid(gridList)
        return grid.filter { grid[it] == 0 }.sumOf { traceTrailNumber(it, grid) }.toString()
    }
}