package me.salmonmoses.days.twentyfive

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single

@Single
@Day(12, 2025)
class Day12 : DayTask {
    override val spec1: TaskSpec?
        get() = null
    override val spec2: TaskSpec?
        get() = null

    override fun task1(
        input: List<String>,
        params: ParamsMap
    ): String = input.reversed()
            .takeWhile { it.isNotBlank() }
            .count { row ->
                val parts = row.split(":")
                val area = parts[0].trim().split("x").fold(1L) { acc, a -> acc * a.toLong() }
                val presentsSum = parts[1].trim().split(" ").sumOf { it.toLong() }
                area >= presentsSum * 9L
            }.toString()

    override fun task2(
        input: List<String>,
        params: ParamsMap
    ): String {
        TODO("")
    }
}