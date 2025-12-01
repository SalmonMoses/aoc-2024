package me.salmonmoses.days.twentyfour

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single

@Single
@Day(19, 2024)
class Day19 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
            "r, wr, b, g, bwu, rb, gb, br\n" +
                    "\n" +
                    "brwrr\n" +
                    "bggr\n" +
                    "gbbr\n" +
                    "rrbgbr\n" +
                    "ubwu\n" +
                    "bwurrg\n" +
                    "brgr\n" +
                    "bbrgwb", "6"
        )
    override val spec2: TaskSpec?
        get() = TaskSpec(
            "r, wr, b, g, bwu, rb, gb, br\n" +
                    "\n" +
                    "brwrr\n" +
                    "bggr\n" +
                    "gbbr\n" +
                    "rrbgbr\n" +
                    "ubwu\n" +
                    "bwurrg\n" +
                    "brgr\n" +
                    "bbrgwb", "16"
        )

    private data class MemoizedTowelQuery(
        val remainingPattern: String,
        val patterns: Set<String>,
        val patternLengthRange: IntRange
    )

    private val memoizedTowelPossibility = mutableMapOf<MemoizedTowelQuery, Boolean>()
    private val memoizedTowelsVariants = mutableMapOf<MemoizedTowelQuery, Long>()

    private fun isPossible(remainingPattern: String, patterns: Set<String>, patternLengthRange: IntRange): Boolean {
        val memoizationQuery = MemoizedTowelQuery(remainingPattern, patterns, patternLengthRange)
        if (memoizationQuery in memoizedTowelPossibility) return memoizedTowelPossibility[memoizationQuery]!!
        if (remainingPattern.isEmpty()) {
            memoizedTowelPossibility[memoizationQuery] = false
            return false
        }
        if (remainingPattern in patterns) {
            memoizedTowelPossibility[memoizationQuery] = true
            return true
        }
        var possible = false
        var nextPatternLength = patternLengthRange.first
        while (nextPatternLength in patternLengthRange && nextPatternLength <= remainingPattern.length && !possible) {
            if (remainingPattern.substring(0, nextPatternLength) !in patterns) {
                nextPatternLength++
                continue
            }
            possible = isPossible(remainingPattern.drop(nextPatternLength), patterns, patternLengthRange)
            nextPatternLength++
        }
        memoizedTowelPossibility[memoizationQuery] = possible
        return possible
    }

    private fun getAllVariants(remainingPattern: String, patterns: Set<String>, patternLengthRange: IntRange): Long {
        val memoizationQuery = MemoizedTowelQuery(remainingPattern, patterns, patternLengthRange)
        if (memoizationQuery in memoizedTowelsVariants) return memoizedTowelsVariants[memoizationQuery]!!
        var variants = 0L
        if (remainingPattern.isEmpty() || !isPossible(remainingPattern, patterns, patternLengthRange)) {
            memoizedTowelsVariants[memoizationQuery] = 0
            return 0
        }
        if (remainingPattern in patterns) {
            variants++
        }
        var nextPatternLength = patternLengthRange.first
        while (nextPatternLength in patternLengthRange && nextPatternLength <= remainingPattern.length) {
            if (remainingPattern.substring(0, nextPatternLength) !in patterns) {
                nextPatternLength++
                continue
            }
            variants += getAllVariants(remainingPattern.drop(nextPatternLength), patterns, patternLengthRange)
            nextPatternLength++
        }
        memoizedTowelsVariants[memoizationQuery] = variants
        return variants
    }

    override fun task1(input: List<String>): String {
        val patterns = input[0].split(",").map(String::trim).toSet()
        val minPatternLength = patterns.minOf(String::length)
        val maxPatternLength = patterns.maxOf(String::length)
        return input.drop(2).count { isPossible(it, patterns, minPatternLength..maxPatternLength) }.toString()
    }

    override fun task2(input: List<String>): String {
        val patterns = input[0].split(",").map(String::trim).toSet()
        val minPatternLength = patterns.minOf(String::length)
        val maxPatternLength = patterns.maxOf(String::length)
        return input.drop(2).sumOf { getAllVariants(it, patterns, minPatternLength..maxPatternLength) }.toString()
    }
}