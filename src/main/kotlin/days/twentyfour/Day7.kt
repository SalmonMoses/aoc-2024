package me.salmonmoses.days.twentyfour

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single

typealias Equation = Pair<Long, List<Long>>

@Single
@Day(7, 2024)
class Day7 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
                "190: 10 19\n" +
                        "3267: 81 40 27\n" +
                        "83: 17 5\n" +
                        "156: 15 6\n" +
                        "7290: 6 8 6 15\n" +
                        "161011: 16 10 13\n" +
                        "192: 17 8 14\n" +
                        "21037: 9 7 18 13\n" +
                        "292: 11 6 16 20", "3749"
        )
    override val spec2: TaskSpec
        get() = TaskSpec(
                "190: 10 19\n" +
                        "3267: 81 40 27\n" +
                        "83: 17 5\n" +
                        "156: 15 6\n" +
                        "7290: 6 8 6 15\n" +
                        "161011: 16 10 13\n" +
                        "192: 17 8 14\n" +
                        "21037: 9 7 18 13\n" +
                        "292: 11 6 16 20", "11387"
        )

    private fun equationCanBeValid(result: Long, acc: Long, remainedCoefs: List<Long>): Boolean {
        if (remainedCoefs.isEmpty()) {
            return result == acc
        }
        return equationCanBeValid(
                result,
                acc * remainedCoefs[0],
                remainedCoefs.drop(1)
        ) || equationCanBeValid(result, acc + remainedCoefs[0], remainedCoefs.drop(1))
    }

    private fun equationCanBeValidWithConcatenation(result: Long, acc: Long, remainedCoefs: List<Long>): Boolean {
        if (remainedCoefs.isEmpty()) {
            return result == acc
        }
        return equationCanBeValidWithConcatenation(
                result,
                acc * remainedCoefs[0],
                remainedCoefs.drop(1)
        ) || equationCanBeValidWithConcatenation(
                result,
                acc + remainedCoefs[0],
                remainedCoefs.drop(1)
        ) || equationCanBeValidWithConcatenation(
                result,
                "$acc${remainedCoefs[0]}".toLong(),
                remainedCoefs.drop(1)
        )
    }

    private fun parseEquation(line: String): Equation {
        val equationParts = line.split(":").map(String::trim)
        val result = equationParts[0].toLong()
        val coefs = equationParts[1].split(" ").map(String::toLong)
        return result to coefs
    }

    override fun task1(input: List<String>, params: ParamsMap): String {
        return input.map(::parseEquation)
                .filter { equationCanBeValid(it.first, it.second[0], it.second.drop(1)) }
                .sumOf(Equation::first)
                .toString()
    }

    override fun task2(input: List<String>, params: ParamsMap): String {
        return input.map(::parseEquation)
                .filter { equationCanBeValidWithConcatenation(it.first, it.second[0], it.second.drop(1)) }
                .sumOf(Equation::first)
                .toString()
    }
}