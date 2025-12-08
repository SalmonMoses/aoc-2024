package me.salmonmoses.days.twentyfive

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single
import kotlin.math.pow

@Single
@Day(3, 2025)
class Day3 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
            "987654321111111\n" +
                    "811111111111119\n" +
                    "234234234234278\n" +
                    "818181911112111", "357"
        )
    override val spec2: TaskSpec
        get() = TaskSpec(
            "987654321111111\n" +
                    "811111111111119\n" +
                    "234234234234278\n" +
                    "818181911112111", "3121910778619"
        )

    private fun findMaxInRow(row: String, length: Int): Long {
        val digits: Array<Int> = Array(length) { 0 }
        var lastDigitIndex = -1
        for (i in 0..<length) {
            for (j in (lastDigitIndex + 1)..(row.length - length + i)) {
                val currentDigit = row[j].digitToInt()
                if (currentDigit > digits[i]) {
                    digits[i] = currentDigit
                    lastDigitIndex = j
                }
            }
        }
        return digits.withIndex().sumOf { (i, digit) -> digit * 10.0.pow(length - i - 1) }.toLong()
    }

    override fun task1(input: List<String>, params: ParamsMap): String {
        return input.sumOf { findMaxInRow(it, 2) }.toString()
    }

    override fun task2(input: List<String>, params: ParamsMap): String {
        return input.sumOf { findMaxInRow(it, 12) }.toString()
    }
}