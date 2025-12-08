package me.salmonmoses.days.twentyfive

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import me.salmonmoses.utils.rangesOverlap
import org.koin.core.annotation.Single
import kotlin.math.max
import kotlin.math.min

@Single
@Day(5, 2025)
class Day5 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
                "3-5\n" +
                        "10-14\n" +
                        "16-20\n" +
                        "12-18\n" +
                        "\n" +
                        "1\n" +
                        "5\n" +
                        "8\n" +
                        "11\n" +
                        "17\n" +
                        "32", "3"
        )
    override val spec2: TaskSpec
        get() = TaskSpec(
                "3-5\n" +
                        "10-14\n" +
                        "16-20\n" +
                        "12-18", "14"
        )

    private fun mergeRanges(ranges: List<LongRange>): List<LongRange> {
        val mergedRanges = mutableListOf<LongRange>()
        ranges.forEach { range ->
            var foundRangeToMerge = false
            for ((i, mergedRange) in mergedRanges.withIndex()) {
                if (rangesOverlap(range, mergedRange)) {
                    mergedRanges[i] = min(mergedRange.first, range.first)..max(mergedRange.last, range.last)
                    foundRangeToMerge = true
                }
            }
            if (!foundRangeToMerge) {
                mergedRanges.add(range)
            }
        }
        return mergedRanges
    }

    override fun task1(input: List<String>, params: ParamsMap): String {
        val freshIds = input.takeWhile { it != "" }
                .map { it.split("-") }
                .map { it[0].toLong()..it[1].toLong() }
                .toList()
        return input.dropWhile { it != "" }
                .drop(1)
                .map { it.toLong() }
                .count { freshIds.any { range -> it in range } }
                .toString()
    }

    override fun task2(input: List<String>, params: ParamsMap): String {
        val freshIds = input.takeWhile { it != "" }
                .map { it.split("-") }
                .map { it[0].toLong()..it[1].toLong() }
                .toList()
        var mergedRanges = freshIds
        var completedMerging = false
        do {
            val newMergedRanges = mergeRanges(mergedRanges)
            completedMerging = mergedRanges.size == newMergedRanges.size
            mergedRanges = newMergedRanges
        } while (!completedMerging)
        return mergedRanges.sumOf { it.last + 1 - it.first }.toString()
    }
}