package me.salmonmoses.days.twentyfive

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single

@Single
@Day(12, 2025)
class Day12 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
            "", ""
        )
    override val spec2: TaskSpec?
        get() = null

    override fun task1(
        input: List<String>,
        params: ParamsMap
    ): String {
        return ""
    }

    override fun task2(
        input: List<String>,
        params: ParamsMap
    ): String {
        TODO("")
    }
}