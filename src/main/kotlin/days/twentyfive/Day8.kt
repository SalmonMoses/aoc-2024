package me.salmonmoses.days.twentyfive

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import me.salmonmoses.geometry.Vector3
import me.salmonmoses.utils.cartesianProductWithoutRepeats
import org.koin.core.annotation.Single

private data class JunctionBox(val coords: Vector3, var circuit: Circuit) {
    override fun hashCode(): Int {
        return coords.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JunctionBox

        if (coords != other.coords) return false
        if (circuit != other.circuit) return false

        return true
    }
}

private typealias Circuit = MutableSet<JunctionBox>

@Single
@Day(8, 2025)
class Day8 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
                "162,817,812\n" +
                        "57,618,57\n" +
                        "906,360,560\n" +
                        "592,479,940\n" +
                        "352,342,300\n" +
                        "466,668,158\n" +
                        "542,29,236\n" +
                        "431,825,988\n" +
                        "739,650,466\n" +
                        "52,470,668\n" +
                        "216,146,977\n" +
                        "819,987,18\n" +
                        "117,168,530\n" +
                        "805,96,715\n" +
                        "346,949,466\n" +
                        "970,615,88\n" +
                        "941,993,340\n" +
                        "862,61,35\n" +
                        "984,92,344\n" +
                        "425,690,689", "40", mapOf("n" to 10)
        )
    override val spec2: TaskSpec
        get() = TaskSpec(
                "162,817,812\n" +
                        "57,618,57\n" +
                        "906,360,560\n" +
                        "592,479,940\n" +
                        "352,342,300\n" +
                        "466,668,158\n" +
                        "542,29,236\n" +
                        "431,825,988\n" +
                        "739,650,466\n" +
                        "52,470,668\n" +
                        "216,146,977\n" +
                        "819,987,18\n" +
                        "117,168,530\n" +
                        "805,96,715\n" +
                        "346,949,466\n" +
                        "970,615,88\n" +
                        "941,993,340\n" +
                        "862,61,35\n" +
                        "984,92,344\n" +
                        "425,690,689", "25272"
        )

    override fun task1(input: List<String>, params: ParamsMap): String {
        val junctionBoxes = input.map { it.split(",") }
                .map { JunctionBox(Vector3(it[0].toInt(), it[1].toInt(), it[2].toInt()), mutableSetOf()) }
        junctionBoxes.forEach { jb -> jb.circuit.add(jb) }
        val circuits = junctionBoxes.map { it.circuit }.toMutableList()

        val closestPairs = junctionBoxes.cartesianProductWithoutRepeats()
                .sortedBy { (first, second) -> first.coords.sqrtEuclidean(second.coords) }
                .take((params["n"] as? Int) ?: 1000)
        for (pair in closestPairs) {
            if (pair.first.circuit !== pair.second.circuit) {
                circuits.remove(pair.second.circuit)
                pair.first.circuit.addAll(pair.second.circuit)
                pair.second.circuit.forEach { it.circuit = pair.first.circuit }
            }
        }

        return circuits.sortedByDescending { it.size }
                .take(3)
                .fold(1) { acc, c -> acc * c.size }
                .toString()
    }

    override fun task2(input: List<String>, params: ParamsMap): String {
        val junctionBoxes = input.map { it.split(",") }
                .map { JunctionBox(Vector3(it[0].toInt(), it[1].toInt(), it[2].toInt()), mutableSetOf()) }
        junctionBoxes.forEach { jb -> jb.circuit.add(jb) }
        val circuits = junctionBoxes.map { it.circuit }.toMutableList()

        val closestPairs = junctionBoxes.cartesianProductWithoutRepeats()
                .sortedBy { (first, second) -> first.coords.sqrtEuclidean(second.coords) }
        var i = 0
        while (i < closestPairs.size && circuits.size > 1) {
            val pair = closestPairs[i]
            if (pair.first.circuit !== pair.second.circuit) {
                circuits.remove(pair.second.circuit)
                pair.first.circuit.addAll(pair.second.circuit)
                pair.second.circuit.forEach { it.circuit = pair.first.circuit }
            }
            i++
        }

        return (closestPairs[i - 1].first.coords.x * closestPairs[i - 1].second.coords.x).toString()
    }
}