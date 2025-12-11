package me.salmonmoses.days.twentyfive

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single

@Single
@Day(11, 2025)
class Day11 : DayTask {
    override val spec1: TaskSpec
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
    override val spec2: TaskSpec
        get() = TaskSpec(
            "svr: aaa bbb\n" +
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
                    "hhh: out", "2"
        )

    fun tracePaths(
        graph: Map<String, List<String>>,
        start: String,
        end: String,
        visited: Set<String>,
        memoized: MutableMap<Pair<String, String>, Long>
    ): Long {
        val startEndPair = Pair(start, end)
        if (startEndPair in memoized) {
            return memoized[startEndPair]!!
        }

        if (start == end) {
            memoized[startEndPair] = 1L
            return 1L
        }

        val connections = graph[start] ?: return 0L
        var pathsFromHere = 0L
        for (connection in connections.filter { it !in visited }) {
            pathsFromHere += tracePaths(graph, connection, end, visited + start, memoized)
        }
        memoized[startEndPair] = pathsFromHere
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

        val memoized: MutableMap<Pair<String, String>, Long> = mutableMapOf()
        return tracePaths(graph, "you", "out", setOf(), memoized).toString()
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

        val memoized: MutableMap<Pair<String, String>, Long> = mutableMapOf()
        val svrFft = tracePaths(graph, "svr", "fft", setOf(), memoized)
        val fftDac = tracePaths(graph, "fft", "dac", setOf(), memoized)
        val dacOut = tracePaths(graph, "dac", "out", setOf(), memoized)
        val svrDac = tracePaths(graph, "svr", "dac", setOf(), memoized)
        val dacFft = tracePaths(graph, "dac", "fft", setOf(), memoized)
        val fftOut = tracePaths(graph, "fft", "out", setOf(), memoized)
        return (svrFft * fftDac * dacOut + svrDac * dacFft * fftOut).toString()
    }
}