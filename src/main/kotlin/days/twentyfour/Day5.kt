package me.salmonmoses.days.twentyfour

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single

@Single
@Day(5, 2024)
class Day5 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
                "47|53\n" +
                        "97|13\n" +
                        "97|61\n" +
                        "97|47\n" +
                        "75|29\n" +
                        "61|13\n" +
                        "75|53\n" +
                        "29|13\n" +
                        "97|29\n" +
                        "53|29\n" +
                        "61|53\n" +
                        "97|53\n" +
                        "61|29\n" +
                        "47|13\n" +
                        "75|47\n" +
                        "97|75\n" +
                        "47|61\n" +
                        "75|61\n" +
                        "47|29\n" +
                        "75|13\n" +
                        "53|13\n" +
                        "\n" +
                        "75,47,61,53,29\n" +
                        "97,61,53,29,13\n" +
                        "75,29,13\n" +
                        "75,97,47,61,53\n" +
                        "61,13,29\n" +
                        "97,13,75,29,47", "143"
        )
    override val spec2: TaskSpec
        get() = TaskSpec(
                "47|53\n" +
                        "97|13\n" +
                        "97|61\n" +
                        "97|47\n" +
                        "75|29\n" +
                        "61|13\n" +
                        "75|53\n" +
                        "29|13\n" +
                        "97|29\n" +
                        "53|29\n" +
                        "61|53\n" +
                        "97|53\n" +
                        "61|29\n" +
                        "47|13\n" +
                        "75|47\n" +
                        "97|75\n" +
                        "47|61\n" +
                        "75|61\n" +
                        "47|29\n" +
                        "75|13\n" +
                        "53|13\n" +
                        "\n" +
                        "75,47,61,53,29\n" +
                        "97,61,53,29,13\n" +
                        "75,29,13\n" +
                        "75,97,47,61,53\n" +
                        "61,13,29\n" +
                        "97,13,75,29,47", "123"
        )

    override fun task1(input: List<String>, params: ParamsMap): String {
        val rules = mutableMapOf<Int, MutableList<Int>>()

        var line = 0
        while (line < input.size) {
            val rule = input[line]
            if (rule == "") {
                break
            }

            val tokens = rule.split("|").map(String::toInt)
            rules.putIfAbsent(tokens[1], mutableListOf())
            rules[tokens[1]]!!.add(tokens[0])
            line++
        }

        line++
        var sum = 0
        while (line < input.size) {
            val pages = input[line].split(",").map(String::toInt)
            var isValid = true
            for (pageIndex in pages.indices) {
                val pagesThatMustBeBefore = rules[pages[pageIndex]] ?: continue
                for (checkIndex in (pageIndex)..<pages.size) {
                    if (pages[checkIndex] in pagesThatMustBeBefore) {
                        isValid = false
                        break
                    }
                }

                if (!isValid) {
                    break
                }
            }

            if (isValid) {
                sum += pages[(pages.size) / 2]
            }

            line++
        }

        return sum.toString()
    }

    override fun task2(input: List<String>, params: ParamsMap): String {
        val rules = mutableMapOf<Int, MutableList<Int>>()

        var line = 0
        while (line < input.size) {
            val rule = input[line]
            if (rule == "") {
                break
            }

            val tokens = rule.split("|").map(String::toInt)
            rules.putIfAbsent(tokens[1], mutableListOf())
            rules[tokens[1]]!!.add(tokens[0])
            line++
        }

        line++
        var sum = 0
        while (line < input.size) {
            val pages = input[line].split(",").map(String::toInt)
            var isValid = true
            for (pageIndex in pages.indices) {
                val pagesThatMustBeBefore = rules[pages[pageIndex]] ?: continue
                for (checkIndex in (pageIndex)..<pages.size) {
                    if (pages[checkIndex] in pagesThatMustBeBefore) {
                        isValid = false
                        break
                    }
                }
            }

            if (!isValid) {
                val rightSortedPages = pages.sortedWith { p1, p2 ->
                    when (true) {
                        (rules[p2]?.contains(p1)) -> -1
                        (rules[p1]?.contains(p2)) -> 1
                        else -> 0
                    }
                }
                sum += rightSortedPages[(rightSortedPages.size) / 2]
            }

            line++
        }

        return sum.toString()
    }
}