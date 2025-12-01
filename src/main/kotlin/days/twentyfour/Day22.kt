package me.salmonmoses.days

import org.koin.core.annotation.Single

@Single
@Day(22, 2024)
class Day22 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec("1\n" +
                "10\n" +
                "100\n" +
                "2024", "37327623")
    override val spec2: TaskSpec?
        get() = TaskSpec("1\n" +
                "2\n" +
                "3\n" +
                "2024", "23")

    private val pruneMod = 16777216uL

    override fun task1(input: List<String>): String =
        input.map(String::toULong).map {
            var currentSecret = it
            repeat(2000) {
                currentSecret = ((currentSecret shl 6) xor currentSecret) % pruneMod
                currentSecret = ((currentSecret shr 5) xor currentSecret) % pruneMod
                currentSecret = ((currentSecret shl 11) xor currentSecret) % pruneMod
            }
            currentSecret
        }.reduce(ULong::plus).toString()

    override fun task2(input: List<String>): String = input.map(String::toULong).map {
        var currentSecret = it
        repeat(2000) {
            currentSecret = ((currentSecret shl 6) xor currentSecret) % pruneMod
            currentSecret = ((currentSecret shr 5) xor currentSecret) % pruneMod
            currentSecret = ((currentSecret shl 11) xor currentSecret) % pruneMod
        }
        currentSecret
    }.zipWithNext().toString()
}