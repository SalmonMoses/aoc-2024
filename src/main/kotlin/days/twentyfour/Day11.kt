package me.salmonmoses.days.twentyfour

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single

@Single
@Day(11, 2024)
class Day11 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec("0 1 10 99 999", "55312")
    override val spec2: TaskSpec?
        get() = null

    private fun task(input: String, steps: Int): Long {
        var stones = input.split(" ")
            .map(String::toLong)
            .groupBy { it }
            .mapValues { it.value.size.toLong() }
        repeat(steps) {
            stones = blink(stones)
        }
        return stones.values.sum()
    }

    private fun blink(stones: Map<Long, Long>): Map<Long, Long> {
        val newStones = mutableMapOf<Long, Long>()
        stones.forEach { (stone, amount) ->
            val digits = stone.toString()
            if (stone == 0L) {
                newStones.merge(1L, amount, Long::plus)
            } else if (digits.length % 2 == 0) {
                val sizeMiddle = digits.length / 2
                val left = digits.substring(0, sizeMiddle).toLong()
                val right = digits.substring(sizeMiddle).toLong()
                newStones.merge(left, amount, Long::plus)
                newStones.merge(right, amount, Long::plus)
            } else {
                val multipliedStone = stone * 2024
                newStones.merge(multipliedStone, amount, Long::plus)
            }
        }
        return newStones
    }

    override fun task1(input: List<String>): String {
        return task(input[0], 25).toString()
    }

    override fun task2(input: List<String>): String {
        return task(input[0], 75).toString()
    }
}