package me.salmonmoses.days

import me.salmonmoses.utils.Colored
import me.salmonmoses.utils.OutputColor
import org.koin.core.annotation.Single
import kotlin.math.absoluteValue

@Single
@Day(2)
class Day2 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
            "7 6 4 2 1\n" +
                    "1 2 7 8 9\n" +
                    "9 7 6 2 1\n" +
                    "1 3 2 4 5\n" +
                    "8 6 4 4 1\n" +
                    "1 3 6 7 9", "2"
        )
    override val spec2: TaskSpec?
        get() = TaskSpec(
            "7 6 4 2 1\n" +
                    "1 2 7 8 9\n" +
                    "9 7 6 2 1\n" +
                    "1 3 2 4 5\n" +
                    "8 6 4 4 1\n" +
                    "1 3 6 7 9", "4"
        )

    fun isSafe(levels: List<Int>): Boolean {
        val increasingReport = levels[0] < levels[1]
        if (levels[1] == levels[0]) return false
        if ((levels[1] - levels[0]).absoluteValue > 3) return false
        for (i in 1..<(levels.size - 1)) {
            val increasingLevels = levels[i] < levels[i + 1]
            if (levels[i] == levels[i + 1]) return false
            if (increasingLevels != increasingReport) return false
            if ((levels[i] - levels[i + 1]).absoluteValue > 3) return false
        }
        return true
    }

    override fun task1(input: List<String>): String {
        return input.map { it.split(" ").map(String::toInt) }.count(::isSafe).toString()
    }

    override fun task2(input: List<String>): String {
        return input.map { it.split(" ").map(String::toInt) }.count {
            for (i in it.indices) {
                if (isSafe(it.filterIndexed { index, _ -> index != i })) {
                    return@count true
                }
            }
            return@count false
        }.toString()
    }
}