package me.salmonmoses.days.twentyfour

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single

@Single
@Day(19)
class Day19 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec("", "")
    override val spec2: TaskSpec?
        get() = null

    override fun task1(input: List<String>): String {
        TODO()
    }

    override fun task2(input: List<String>): String {
        TODO()
    }
}