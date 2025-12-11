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

    val memoized: MutableMap<Pair<String, String>, Long> = mutableMapOf()

    fun tracePaths(graph: Map<String, List<String>>, start: String, end: String, visited: Set<String>): Long {
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
            pathsFromHere += tracePaths(graph, connection, end, visited + start)
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

        memoized.clear()
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

        memoized.clear()
        val svrFft = tracePaths(graph, "svr", "fft", setOf())
        val fftDac = tracePaths(graph, "fft", "dac", setOf())
        val dacOut = tracePaths(graph, "dac", "out", setOf())
        val svrDac = tracePaths(graph, "svr", "dac", setOf())
        val dacFft = tracePaths(graph, "dac", "fft", setOf())
        val fftOut = tracePaths(graph, "fft", "out", setOf())
        return (svrFft * fftDac * dacOut + svrDac * dacFft * fftOut).toString()
    }
}