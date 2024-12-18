package me.salmonmoses.days.twentyfour

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single
import kotlin.math.abs

@Single
@Day(1)
class Day1 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
            "3   4\n" +
                    "4   3\n" +
                    "2   5\n" +
                    "1   3\n" +
                    "3   9\n" +
                    "3   3", "11"
        )
    override val spec2: TaskSpec?
        get() = TaskSpec(
            "3   4\n" +
                    "4   3\n" +
                    "2   5\n" +
                    "1   3\n" +
                    "3   9\n" +
                    "3   3", "31"
        )

    override fun task1(input: List<String>): String {
        val coordsLists = input.map {
            val coords = it.split("   ")
            Pair(coords[0].toInt(), coords[1].toInt())
        }.unzip()
        val sortedX = coordsLists.first.sorted()
        val sortedY = coordsLists.second.sorted()
        return sortedX.zip(sortedY)
            .sumOf { (x, y) -> abs(x - y) }
            .toString()
    }

    override fun task2(input: List<String>): String {
        val coordsLists = input.map {
            val coords = it.split("   ")
            Pair(coords[0].toInt(), coords[1].toInt())
        }.unzip()
        return coordsLists.first
            .sumOf { x -> x * coordsLists.second.count { it == x } }
            .toString()
    }
}