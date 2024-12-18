package me.salmonmoses.days.nineteen

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single

@Single
@Day(2, 2019)
class Day2 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec("1,9,10,3,2,3,11,0,99,30,40,50", "3500")
    override val spec2: TaskSpec?
        get() = null

    override fun task1(input: List<String>): String {
        val memory = input[0].split(',').map(String::toInt)
        val computer = Computer(memory)
        computer()
        return computer.getAtPointer(0).toString()
    }

    override fun task2(input: List<String>): String {
        val memory = input[0].split(',').map(String::toInt).toMutableList()
        (0..99).forEach { noun ->
            (0..99).forEach { verb ->
                memory[1] = noun
                memory[2] = verb
                val computer = Computer(memory)
                computer()
                if (computer.getAtPointer(0) == 19690720) {
                    return (100 * noun + verb).toString()
                }
            }
        }
        return ""
    }
}