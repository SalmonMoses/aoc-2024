package me.salmonmoses.days

import me.salmonmoses.utils.Vector
import org.koin.core.annotation.Single

@Single
@Day(8)
class Day8 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
            "............\n" +
                    "........0...\n" +
                    ".....0......\n" +
                    ".......0....\n" +
                    "....0.......\n" +
                    "......A.....\n" +
                    "............\n" +
                    "............\n" +
                    "........A...\n" +
                    ".........A..\n" +
                    "............\n" +
                    "............", "14"
        )
    override val spec2: TaskSpec?
        get() = TaskSpec(
            "............\n" +
                    "........0...\n" +
                    ".....0......\n" +
                    ".......0....\n" +
                    "....0.......\n" +
                    "......A.....\n" +
                    "............\n" +
                    "............\n" +
                    "........A...\n" +
                    ".........A..\n" +
                    "............\n" +
                    "............", "34"
        )

    fun getNodes(input: List<String>): Map<Char, List<Vector>> {
        val nodes = mutableMapOf<Char, MutableList<Vector>>()
        input.indices.forEach { y ->
            input[y].indices.forEach { x ->
                val frequency = input[y][x]
                if (frequency != '.') {
                    if (nodes[frequency] == null) {
                        nodes[frequency] = mutableListOf()
                    }
                    nodes[frequency]!!.add(Vector(x, y))
                }
            }
        }
        return nodes.map { Pair(it.key, it.value.toList()) }.toMap()
    }

    override fun task1(input: List<String>): String {
        val nodes = getNodes(input)
        val size = input.size
        val antinodes = mutableSetOf<Vector>()
        nodes.forEach { (_, antennas) ->
            antennas.forEachIndexed { first, firstAntenna ->
                ((first + 1)..<antennas.size).forEach { second ->
                    val secondAntenna = antennas[second]
                    val direction = secondAntenna - firstAntenna
                    antinodes.add(firstAntenna - direction)
                    antinodes.add(secondAntenna + direction)
                }
            }
        }
        return antinodes.filter { it.x in 0..<size && it.y in 0..<size }.size.toString()
    }

    override fun task2(input: List<String>): String {
        val nodes = getNodes(input)
        val size = input.size
        val antinodes = mutableSetOf<Vector>()
        nodes.forEach { (_, antennas) ->
            antennas.forEachIndexed { first, firstAntenna ->
                ((first + 1)..<antennas.size).forEach { second ->
                    val secondAntenna = antennas[second]
                    val direction = secondAntenna - firstAntenna
                    antinodes.add(firstAntenna)
                    antinodes.add(secondAntenna)
                    var antinodeLocation = firstAntenna - direction
                    while (antinodeLocation.x >= 0 && antinodeLocation.y >= 0) {
                        antinodes.add(antinodeLocation)
                        antinodeLocation -= direction
                    }
                    antinodeLocation = secondAntenna + direction
                    while (antinodeLocation.x < size && antinodeLocation.y < size) {
                        antinodes.add(antinodeLocation)
                        antinodeLocation += direction
                    }
                }
            }
        }
        return antinodes.filter { it.x in 0..<size && it.y in 0..<size }.size.toString()
    }
}