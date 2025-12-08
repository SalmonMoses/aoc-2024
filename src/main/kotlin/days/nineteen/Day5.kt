package me.salmonmoses.days.nineteen

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single

@Single
@Day(5, 2019)
class Day5 : DayTask {
    override val spec1: TaskSpec?
        get() = TaskSpec("3,0,4,0,99", "1")
    override val spec2: TaskSpec?
        get() = null

    override fun task1(input: List<String>, params: ParamsMap): String {
        val memory = input[0].split(',').map(String::toInt)
        val computer = Computer(memory)
        computer.giveInput(1)
        computer()
        return computer.lastOutput.toString()
    }

    override fun task2(input: List<String>, params: ParamsMap): String {
        val memory = input[0].split(',').map(String::toInt)
        val computer = Computer(memory)
        computer.giveInput(5)
        computer()
        return computer.lastOutput.toString()
    }
}