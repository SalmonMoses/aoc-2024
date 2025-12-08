package me.salmonmoses.days

import org.koin.core.annotation.Single

@Single
@Day(0, 0)
class Day0 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec("", "")
    override val spec2: TaskSpec?
        get() = null

    override fun task1(input: List<String>, params: ParamsMap): String {
        TODO()
    }

    override fun task2(input: List<String>, params: ParamsMap): String {
        TODO()
    }
}