package me.salmonmoses.days

import me.salmonmoses.utils.isInteger
import org.koin.core.annotation.Single

@Single
@Day(13)
class Day13 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
            "Button A: X+94, Y+34\n" +
                    "Button B: X+22, Y+67\n" +
                    "Prize: X=8400, Y=5400\n" +
                    "\n" +
                    "Button A: X+26, Y+66\n" +
                    "Button B: X+67, Y+21\n" +
                    "Prize: X=12748, Y=12176\n" +
                    "\n" +
                    "Button A: X+17, Y+86\n" +
                    "Button B: X+84, Y+37\n" +
                    "Prize: X=7870, Y=6450\n" +
                    "\n" +
                    "Button A: X+69, Y+23\n" +
                    "Button B: X+27, Y+71\n" +
                    "Prize: X=18641, Y=10279", "480"
        )
    override val spec2: TaskSpec?
        get() = null

    private fun solveArcade(
        ax: Double,
        ay: Double,
        bx: Double,
        by: Double,
        px: Double,
        py: Double
    ): Double {
        /**
         * px = m * ax + n * bx
         * py = m * ay + n * by
         * m = (px - n * bx) / ax
         * py = (px - n * bx) / ax * ay + n * by
         * py = (ay * px - n * ay * bx) / ax + n * by
         * ax * py = ay * px - n * ay * bx + n * ax * by
         * n * ay * bx - n * ax * by = ay * px - ax * py
         * n (ay * bx - ax * by) = ay * px - ax * py
         * n = (ay * px - ax * py) / (ay * bx - ax * by)
         */
        val dividend = (ay * px - ax * py)
        val divisor = (ay * bx - ax * by)
        val n = dividend / divisor
        val m = (px - n * bx) / ax
        return if (m.isInteger() && n.isInteger() && m >= 0 && n >= 0) m * 3 + n else 0.0
    }

    override fun task1(input: List<String>): String {
        val arcadeRegex = Regex(
            "Button A: X[+]([0-9]+), Y[+]([0-9]+)\n" +
                    "Button B: X[+]([0-9]+), Y[+]([0-9]+)\n" +
                    "Prize: X=([0-9]+), Y=([0-9]+)"
        )
        val arcadeDescs = input.filter { it.trim().isNotEmpty() }
            .joinToString("\n")
        return arcadeRegex.findAll(arcadeDescs).map {
            val ax = it.groupValues[1].toDouble()
            val ay = it.groupValues[2].toDouble()
            val bx = it.groupValues[3].toDouble()
            val by = it.groupValues[4].toDouble()
            val prizeX = it.groupValues[5].toDouble()
            val prizeY = it.groupValues[6].toDouble()
            solveArcade(ax, ay, bx, by, prizeX, prizeY)
        }.sumOf { it.toLong() }.toString()
    }

    override fun task2(input: List<String>): String {
        val arcadeRegex = Regex(
            "Button A: X[+]([0-9]+), Y[+]([0-9]+)\n" +
                    "Button B: X[+]([0-9]+), Y[+]([0-9]+)\n" +
                    "Prize: X=([0-9]+), Y=([0-9]+)"
        )
        val arcadeDescs = input.filter { it.trim().isNotEmpty() }
            .joinToString("\n")
        return arcadeRegex.findAll(arcadeDescs).map {
            val ax = it.groupValues[1].toDouble()
            val ay = it.groupValues[2].toDouble()
            val bx = it.groupValues[3].toDouble()
            val by = it.groupValues[4].toDouble()
            val prizeX = it.groupValues[5].toDouble() + 10000000000000
            val prizeY = it.groupValues[6].toDouble() + 10000000000000
            solveArcade(ax, ay, bx, by, prizeX, prizeY)
        }.sumOf { it.toLong() }.toString()
    }
}