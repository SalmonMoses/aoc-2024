package me.salmonmoses.days.twentyfour

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single

@Single
@Day(3)
class Day3 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))", "161")
    override val spec2: TaskSpec?
        get() = TaskSpec("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))", "48")

    override fun task1(input: List<String>): String {
        val inputLine = input[0]
        val regex = Regex("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)")
        val commands = regex.findAll(inputLine)
        return commands.sumOf { cmd -> cmd.groupValues[1].toInt() * cmd.groupValues[2].toInt() }.toString()
    }

    override fun task2(input: List<String>): String {
        val inputLine = input[0]
        val regex = Regex("(mul|do|don't)\\((([0-9]{1,3}),([0-9]{1,3}))?\\)")
        val commands = regex.findAll(inputLine)
        var enabled = true
        var sum = 0
        for (cmd in commands) {
            val mnemonic = cmd.groupValues[1]
            when (mnemonic) {
                "do" -> enabled = true
                "don't" -> enabled = false
                "mul" -> if (enabled) {
                    sum += cmd.groupValues[3].toInt() * cmd.groupValues[4].toInt()
                }
            }
        }
        return sum.toString()
    }
}