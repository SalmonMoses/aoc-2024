package me.salmonmoses.days.nineteen

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import me.salmonmoses.utils.permutations
import org.koin.core.annotation.Single

@Single
@Day(7, 2019)
class Day7 : DayTask {
    override val spec1: TaskSpec?
        get() = null
    override val spec2: TaskSpec
        get() = TaskSpec(
                "3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54," +
                        "-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4," +
                        "53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10", "18216"
        )

    override fun task1(input: List<String>, params: ParamsMap): String {
        val memory = input[0].split(',').map(String::toInt)
        val phaseSettingsSequence = (0..4).toList()
        var highestOutput = 0
        phaseSettingsSequence.permutations().forEach {
            var lastOutput = 0
            it.forEach { phase ->
                val computer = Computer(memory)
                computer.giveInput(phase)
                computer.giveInput(lastOutput)
                computer()
                lastOutput = computer.lastOutput
            }
            if (lastOutput > highestOutput) {
                highestOutput = lastOutput
            }
        }
        return highestOutput.toString()
    }

    override fun task2(input: List<String>, params: ParamsMap): String {
        val memory = input[0].split(',').map(String::toInt)
        val phaseSettingsSequence = (5..9).toList()
        val computers = Array(5, { Computer(memory) })
        var highestOutput = 0
        phaseSettingsSequence.permutations().forEach {
            var lastOutput = 0
            it.forEachIndexed { index, phase ->
                val computer = computers[index]
                computer.reset(memory)
                computer.giveInput(phase)
            }
            repeat(10) {
                val currentComputer = computers[it % computers.size]
                currentComputer.giveInput(lastOutput)
                lastOutput = currentComputer()
            }
            if (lastOutput > highestOutput) {
                highestOutput = lastOutput
            }
        }
        return highestOutput.toString()
    }
}