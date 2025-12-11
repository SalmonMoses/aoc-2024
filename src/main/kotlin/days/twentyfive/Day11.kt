package me.salmonmoses.days.twentyfive

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single

@Single
@Day(11, 2025)
class Day11 : DayTask {
    override val spec1: TaskSpec?
        get() = TaskSpec(
                "aaa: you hhh\n" +
                        "you: bbb ccc\n" +
                        "bbb: ddd eee\n" +
                        "ccc: ddd eee fff\n" +
                        "ddd: ggg\n" +
                        "eee: out\n" +
                        "fff: out\n" +
                        "ggg: out\n" +
                        "hhh: ccc fff iii\n" +
                        "iii: out", "5"
        )
    override val spec2: TaskSpec?
        get() = TaskSpec("svr: aaa bbb\n" +
                "aaa: fft\n" +
                "fft: ccc\n" +
                "bbb: tty\n" +
                "tty: ccc\n" +
                "ccc: ddd eee\n" +
                "ddd: hub\n" +
                "hub: fff\n" +
                "eee: dac\n" +
                "dac: fff\n" +
                "fff: ggg hhh\n" +
                "ggg: out\n" +
                "hhh: out", "2")

    fun tracePaths(graph: Map<String, List<String>>, start: String, end: String, visited: Set<String>): Int {
        if (start == end) return 1

        val connections = graph[start]!!
        var pathsFromHere = 0
        for (connection in connections.filter { it !in visited }) {
            pathsFromHere += tracePaths(graph, connection, end, visited + start)
        }
        return pathsFromHere
    }

    fun findAllPaths(graph: Map<String, List<String>>, start: String, end: String, visited: Set<String>): List<Set<String>> {
        if (start == end) return listOf(visited)

        val connections = graph[start]!!
        val pathsFromHere = mutableListOf<Set<String>>()
        for (connection in connections.filter { it !in visited }) {
            pathsFromHere += findAllPaths(graph, connection, end, visited + start)
        }
        return pathsFromHere
    }

    override fun task1(
            input: List<String>,
            params: ParamsMap
    ): String {
        val graph = input.associate { row ->
            val tokens = row.split(" ")
            val start = tokens[0].substring(0, tokens[0].length - 1)
            start to tokens.drop(1)
        }

        return tracePaths(graph, "you", "out", setOf()).toString()
    }

    override fun task2(
            input: List<String>,
            params: ParamsMap
    ): String {
        val graph = input.associate { row ->
            val tokens = row.split(" ")
            val start = tokens[0].substring(0, tokens[0].length - 1)
            start to tokens.drop(1)
        }

        val requiredNodes = listOf("dac", "fft")
        return findAllPaths(graph, "svr", "out", setOf())
                .count { it.containsAll(requiredNodes) }.toString()
    }
}