package me.salmonmoses.days

import org.koin.core.annotation.Single

@Single
@Day(11)
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
        for (step in 0..<steps) {
            stones = blink(stones)
        }
        return stones.values.sum()
    }

    private fun blink(stones: Map<Long, Long>): Map<Long, Long> {
        val newStones = mutableMapOf<Long, Long>()
        stones.forEach { (stone, amount) ->
            if (stone == 0L) {
                newStones[1L] = newStones[1L]?.plus(amount) ?: amount
            } else if (stone.toString().length % 2 == 0) {
                val digits = stone.toString()
                val sizeMiddle = digits.length / 2
                val left = digits.substring(0, sizeMiddle).toLong()
                val right = digits.substring(sizeMiddle).toLong()
                newStones[left] = newStones[left]?.plus(amount) ?: amount
                newStones[right] = newStones[right]?.plus(amount) ?: amount
            } else {
                newStones[stone * 2024] = newStones[stone * 2024]?.plus(amount) ?: amount
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