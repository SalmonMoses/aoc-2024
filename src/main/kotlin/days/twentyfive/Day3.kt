package me.salmonmoses.days.twentyfive

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single

@Single
@Day(3, 2025)
class Day3 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec("", "")
    override val spec2: TaskSpec
        get() = TODO("Not yet implemented")

    override fun task1(input: List<String>): String {
        TODO("Not yet implemented")
    }

    override fun task2(input: List<String>): String {
        TODO("Not yet implemented")
    }
}