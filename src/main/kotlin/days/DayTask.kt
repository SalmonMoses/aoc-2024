package me.salmonmoses.days

data class TaskSpec(val input: String, val expectedResult: String)

@Target(AnnotationTarget.CLASS)
annotation class Day(val day: Int, val year: Int = 2024)

interface DayTask {
    val spec1: TaskSpec?
    val spec2: TaskSpec?
    fun task1(input: List<String>): String
    fun task2(input: List<String>): String
}