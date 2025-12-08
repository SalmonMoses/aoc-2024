package me.salmonmoses.days

typealias ParamsMap = Map<String, Any>

data class TaskSpec(val input: String, val expectedResult: String, val params: ParamsMap = mapOf())

@Target(AnnotationTarget.CLASS)
annotation class Day(val day: Int, val year: Int)

interface DayTask {
    val spec1: TaskSpec?
    val spec2: TaskSpec?
    fun task1(input: List<String>, params: ParamsMap = mapOf()): String
    fun task2(input: List<String>, params: ParamsMap = mapOf()): String
}