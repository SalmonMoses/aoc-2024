package me.salmonmoses.days.twentyfive

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single
import kotlin.math.ceil
import kotlin.math.log10
import kotlin.math.pow

@Single
@Day(2, 2025)
class Day2 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
                "11-22,95-115,998-1012,1188511880-1188511890,222220-222224," +
                        "1698522-1698528,446443-446449,38593856-38593862,565653-565659," +
                        "824824821-824824827,2121212118-2121212124", "1227775554"
        )
    override val spec2: TaskSpec
        get() = TaskSpec(
                "11-22,95-115,998-1012,1188511880-1188511890,222220-222224," +
                        "1698522-1698528,446443-446449,38593856-38593862,565653-565659," +
                        "824824821-824824827,2121212118-2121212124", "4174379265"
        )

    private fun parseInput(input: String): List<Long> = input.split(",")
            .map { it.split("-") }
            .map { it[0].toLong()..it[1].toLong() }
            .flatten()

    private fun checkId1(id: Long): Boolean {
        val digitsNumber = ceil(log10(id.toDouble())).toInt()
        if (digitsNumber % 2 == 1) {
            return true
        }

        val middle = 10.0.pow(digitsNumber / 2.0).toInt()
        return (id / middle) != (id % middle)
    }

    private fun checkId2(id: Long): Boolean {
        val digitsNumber = ceil(log10(id.toDouble())).toInt()
        val stringId = id.toString()
        val lenghtsToCheck = ((digitsNumber / 2) downTo 1)
        for (i in lenghtsToCheck) {
            if (digitsNumber % i > 0) {
                continue
            }
            val windows = stringId.windowed(i, i)
            val isInvalid = windows.drop(1)
                    .fold(true) { acc, string -> acc && string == windows[0] }
            if (isInvalid) {
                return false
            }
        }
        return true
    }

    fun task(input: String, idChecker: (Long) -> Boolean): String =
            parseInput(input)
                    .map { id -> Pair(id, idChecker(id)) }
                    .filter { !it.second }
                    .sumOf { it.first }.toString()

    override fun task1(input: List<String>, params: ParamsMap): String = task(input[0], this::checkId1)
    override fun task2(input: List<String>, params: ParamsMap): String = task(input[0], this::checkId2)
}