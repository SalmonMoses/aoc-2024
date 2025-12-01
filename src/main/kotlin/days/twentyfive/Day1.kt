package me.salmonmoses.days.twentyfive

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.TaskSpec
import me.salmonmoses.utils.steppedReduce
import org.koin.core.annotation.Single
import kotlin.collections.map
import kotlin.math.absoluteValue
import kotlin.text.substring

@Single
@Day(1, 2025)
class Day1 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
            "L68\n" +
                    "L30\n" +
                    "R48\n" +
                    "L5\n" +
                    "R60\n" +
                    "L55\n" +
                    "L1\n" +
                    "L99\n" +
                    "R14\n" +
                    "L82", "3"
        )
    override val spec2: TaskSpec?
        get() = TaskSpec(
            "L68\n" +
                    "L30\n" +
                    "R48\n" +
                    "L5\n" +
                    "R60\n" +
                    "L55\n" +
                    "L1\n" +
                    "L99\n" +
                    "R14\n" +
                    "L82", "6"
        )

    private fun parseInput(input: List<String>): Iterable<Int> = input
        .map { Pair(it[0], it.substring(1)) }
        .map { (dir, step) -> (if (dir == 'R') 1 else -1) * step.toInt() }

    override fun task1(input: List<String>): String =
        parseInput(input)
            .steppedReduce(50)
            { current, step ->
                (current + step).mod(100)
            }.count { it == 0 }
            .toString()

    override fun task2(input: List<String>): String {
        val input = parseInput(input)
        var currentValue = 50
        var result = 0
        input.forEach {
            val skipNext = currentValue == 0
            val simplifiedStep = it % 100
            result += it.absoluteValue / 100
            currentValue += simplifiedStep
            if ((!skipNext && currentValue !in 1..99) || currentValue == 0) {
                result++
            }
            currentValue = currentValue.mod(100)
        }
        return result.toString()
    }
}