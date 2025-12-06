package me.salmonmoses.days.twentyfive

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single
import kotlin.collections.withIndex

@Single
@Day(6, 2025)
class Day6 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
            "123 328  51 64 \n" +
                   " 45 64  387 23 \n" +
                   "  6 98  215 314\n" +
                   "*   +   *   +", "4277556"
        )
    override val spec2: TaskSpec?
        get() = TaskSpec(
            "123 328  51 64 \n" +
                   " 45 64  387 23 \n" +
                   "  6 98  215 314\n" +
                   "*   +   *   +", "3263827"
        )

    override fun task1(input: List<String>): String {
        val rows = mutableListOf<List<Long>>()
        for (i in 0..<(input.size - 1)) {
            rows += input[i].trim()
                .split(Regex(" +")).map { it.toLong() }.toList()
        }

        return input.last().trim().split(Regex(" +"))
            .map { it[0] }
            .withIndex()
            .sumOf { (i, op) ->
                when (op) {
                    '*' -> rows.map { it[i] }.reduce { acc, i -> acc * i }
                    '+' -> rows.map { it[i] }.reduce { acc, i -> acc + i }
                    else -> 0L
                }
            }
            .toString()
    }

    override fun task2(input: List<String>): String {
        val reversedInput = input.reversed()
        val ops = reversedInput[0].trim().split(Regex(" +")).map { it[0] }
        val length = reversedInput[1].length
        val rows = mutableListOf<List<Long>>()
        var currentRow = mutableListOf<Long>()
        for (i in 0..<length) {
            val number = reversedInput.drop(1)
                .map { it[i] }
                .fold("") { acc, c -> "$c$acc" }
                .trim()
            if (number.isBlank()) {
                rows += currentRow
                currentRow = mutableListOf()
            } else {
                currentRow += number.toLong()
            }
        }
        rows += currentRow
        return ops.zip(rows)
            .sumOf { (op, row) ->
                when (op) {
                    '*' -> row.reduce { acc, i -> acc * i }
                    '+' -> row.reduce { acc, i -> acc + i }
                    else -> 0L
                }
            }
            .toString()
    }
}