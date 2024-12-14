package me.salmonmoses.days

import me.salmonmoses.utils.DequeIterator
import me.salmonmoses.utils.Grid
import me.salmonmoses.utils.Vector
import org.koin.core.annotation.Single

@Single
@Day(12)
class Day12 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
            "RRRRIICCFF\n" +
                    "RRRRIICCCF\n" +
                    "VVRRRCCFFF\n" +
                    "VVRCCCJFFF\n" +
                    "VVVVCJJCFE\n" +
                    "VVIVCCJJEE\n" +
                    "VVIIICJJEE\n" +
                    "MIIIIIJJEE\n" +
                    "MIIISIJEEE\n" +
                    "MMMISSJEEE", "1930"
        )
    override val spec2: TaskSpec?
        get() = TaskSpec(
            "AAAAAA\n" +
                    "AAABBA\n" +
                    "AAABBA\n" +
                    "ABBAAA\n" +
                    "ABBAAA\n" +
                    "AAAAAA", "368"
        )

    private val neighbors = listOf(
        Vector(1, 0),
        Vector(-1, 0),
        Vector(0, 1),
        Vector(0, -1)
    )

    private val corners = listOf(
        listOf(Vector(0, -1), Vector(1, -1), Vector(1, 0)),
        listOf(Vector(0, 1), Vector(1, 1), Vector(1, 0)),
        listOf(Vector(0, -1), Vector(-1, -1), Vector(-1, 0)),
        listOf(Vector(0, 1), Vector(-1, 1), Vector(-1, 0)),
    )

    private data class Region(val points: Set<Vector>, val perimeter: Long) {
        val price = points.size.toLong() * perimeter
    }

    private data class SideRegion(val points: Set<Vector>, val sides: Long) {
        val price = points.size.toLong() * sides
    }

    private fun findRegion(grid: Grid<Char>, start: Vector): Region {
        val visited = mutableSetOf<Vector>()
        val frontier = ArrayDeque<Vector>()
        val region = mutableSetOf<Vector>()
        val regionChar = grid[start]
        var perimeter = 0L
        region.add(start)
        frontier.add(start)
        for (nextPoint in DequeIterator(frontier)) {
            if (nextPoint in visited) {
                continue
            }

            visited.add(nextPoint)
            if (grid[nextPoint] != regionChar) {
                continue
            }

            region.add(nextPoint)

            val neighbors = neighbors.map { nextPoint + it }
            perimeter += neighbors.count { !grid.isValid(it) || grid[it] != regionChar }
            neighbors
                .filter { grid.isValid(it) && it !in visited }
                .forEach { neighbor -> frontier.add(neighbor) }
        }
        return Region(region, perimeter)
    }

    private fun findRegionWithSides(grid: Grid<Char>, start: Vector): SideRegion {
        val visited = mutableSetOf<Vector>()
        val frontier = ArrayDeque<Vector>()
        val region = mutableSetOf<Vector>()
        val regionChar = grid[start]
        var sides = 0L
        region.add(start)
        frontier.add(start)
        for (nextPoint in DequeIterator(frontier)) {
            if (nextPoint in visited) {
                continue
            }

            visited.add(nextPoint)
            if (grid[nextPoint] != regionChar) {
                continue
            }

            region.add(nextPoint)

            sides += corners.map { corner ->
                val cornerNeighbors = corner.map { it + nextPoint }
                val yCorner = !grid.isValid(cornerNeighbors[0]) || grid[cornerNeighbors[0]] != regionChar
                val xCorner = !grid.isValid(cornerNeighbors[2]) || grid[cornerNeighbors[2]] != regionChar
                val xyCorner = !grid.isValid(cornerNeighbors[1]) || grid[cornerNeighbors[1]] != regionChar
                (xCorner && yCorner) || (!xCorner && !yCorner && xyCorner)
            }.count { it }
            grid.getNeighbors(nextPoint)
                .filter { it !in visited }
                .forEach { neighbor -> frontier.add(neighbor) }
        }
        return SideRegion(region, sides)
    }

    override fun task1(input: List<String>): String {
        val grid = Grid(input.map { row -> row.trim().toList() })
        val alreadyInRegion = mutableSetOf<Vector>()
        val regions = mutableListOf<Region>()
        for (point in grid) {
            if (point in alreadyInRegion) {
                continue
            }
            val region = findRegion(grid, point)
            alreadyInRegion.addAll(region.points)
            regions.add(region)
        }
        return regions.sumOf(Region::price).toString()
    }

    override fun task2(input: List<String>): String {
        val grid = Grid(input.map { row -> row.trim().toList() })
        val alreadyInRegion = mutableSetOf<Vector>()
        val regions = mutableListOf<SideRegion>()
        for (point in grid) {
            if (point in alreadyInRegion) {
                continue
            }
            val region = findRegionWithSides(grid, point)
            alreadyInRegion.addAll(region.points)
            regions.add(region)
        }
        return regions.sumOf(SideRegion::price).toString()
    }
}