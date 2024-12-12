package me.salmonmoses.days

import me.salmonmoses.utils.DequeIterator
import me.salmonmoses.utils.Grid
import me.salmonmoses.utils.GridPoint
import org.koin.core.annotation.Single

val neighbors = listOf(
    GridPoint(1, 0),
    GridPoint(-1, 0),
    GridPoint(0, 1),
    GridPoint(0, -1)
)

val corners = listOf(
    listOf(GridPoint(0, -1), GridPoint(1, -1), GridPoint(1, 0)),
    listOf(GridPoint(0, 1), GridPoint(1, 1), GridPoint(1, 0)),
    listOf(GridPoint(0, -1), GridPoint(-1, -1), GridPoint(-1, 0)),
    listOf(GridPoint(0, 1), GridPoint(-1, 1), GridPoint(-1, 0)),
)

data class Region(val points: Set<GridPoint>, val perimeter: Long) {
    val price = points.size.toLong() * perimeter
}

data class SideRegion(val points: Set<GridPoint>, val sides: Long) {
    val price = points.size.toLong() * sides
}

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

    private fun findRegion(grid: Grid<Char>, start: GridPoint): Region {
        val visited = mutableSetOf<GridPoint>()
        val frontier = ArrayDeque<GridPoint>()
        val region = mutableSetOf<GridPoint>()
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

    private fun findRegionWithSides(grid: Grid<Char>, start: GridPoint): SideRegion {
        val visited = mutableSetOf<GridPoint>()
        val frontier = ArrayDeque<GridPoint>()
        val region = mutableSetOf<GridPoint>()
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
                val cornerNeighbors = corner
                    .map { it + nextPoint }
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
        val alreadyInRegion = mutableSetOf<GridPoint>()
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
        val alreadyInRegion = mutableSetOf<GridPoint>()
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