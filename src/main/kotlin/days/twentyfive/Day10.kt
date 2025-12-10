package me.salmonmoses.days.twentyfive

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import me.salmonmoses.utils.DequeIterator
import org.koin.core.annotation.Single
import java.util.PriorityQueue
import kotlin.collections.none

typealias Button = List<Int>
typealias Joltage = List<Int>
typealias MachineState = List<Boolean>

@Single
@Day(10, 2025)
class Day10 : DayTask {
    override val spec1: TaskSpec?
        get() = TaskSpec(
                "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}\n" +
                        "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}\n" +
                        "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}", "7"
        )
    override val spec2: TaskSpec?
        get() = TaskSpec(
                "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}\n" +
                        "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}\n" +
                        "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}", "33"
        )

    data class Machine(val target: MachineState, val buttons: List<Button>, val joltage: List<Int>)

    fun parseMachine(src: String): Machine {
        val splitted = src.split(" ")
        val targetState = splitted[0].substring(1, splitted[0].length - 1)
                .map { it == '#' }
        val buttons = splitted.drop(1).takeWhile { it.startsWith("(") }.map { btn ->
            btn.substring(1, btn.length - 1)
                    .split(",")
                    .map { it.toInt() }
        }
        val joltage = splitted.last().substring(1, splitted.last().length - 1)
                .split(",")
                .map(String::toInt)
        return Machine(targetState, buttons, joltage)
    }

    fun simulateMachineJoltage(machine: Machine): Long {
        val initialState = List(machine.joltage.size, { 0 })
        val visitedStates = mutableMapOf(initialState to 0)

        val nextStates = PriorityQueue<Pair<Joltage, Int>> { first, second -> first.second - second.second }
        nextStates.add(initialState to 0)
        while (nextStates.isNotEmpty()) {
            val state = nextStates.poll().first
            if (state == machine.joltage) {
                break
            }

            val remainingJoltage = machine.joltage.zip(state).map { it.first - it.second }
            val heurestics = remainingJoltage.sum()
            val minPathToCurrentState = visitedStates[state]!!
            val validNextButtons = machine.buttons.filter { btn ->
                btn.none { remainingJoltage[it] == 0 }
            }
            for (button in validNextButtons) {
                val nextState = state.withIndex().map { (i, current) -> if (i in button) current + 1 else current }
                val isValid = nextState.withIndex().all { (i, j) -> j <= machine.joltage[i] }
                if (isValid && (nextState !in visitedStates || visitedStates[nextState]!! > minPathToCurrentState + 1)) {
                    visitedStates[nextState] = minPathToCurrentState + 1
                    nextStates.offer(nextState to (minPathToCurrentState + heurestics))
                }
            }
        }

        return visitedStates[machine.joltage]?.toLong() ?: Long.MAX_VALUE
    }

    fun simulateMachine(machine: Machine): Long {
        val initialState = List(machine.target.size, { false })
        val visitedStates = mutableMapOf(initialState to 0L)

        val nextStates = ArrayDeque<MachineState>()
        nextStates.add(initialState)
        for (state in DequeIterator(nextStates)) {
            val minPathToCurrentState = visitedStates[state]!!
            for (button in machine.buttons) {
                val nextState = state.withIndex().map { (i, current) -> if (i in button) !current else current }
                if (nextState !in visitedStates || visitedStates[nextState]!! > minPathToCurrentState + 1) {
                    visitedStates[nextState] = minPathToCurrentState + 1
                    nextStates.add(nextState)
                }
            }
        }

        return visitedStates[machine.target] ?: Long.MAX_VALUE
    }

    override fun task1(
            input: List<String>,
            params: ParamsMap
    ): String {
        val machines = input.map(this::parseMachine)
        return machines.sumOf { simulateMachine(it) }.toString()
    }

    override fun task2(
            input: List<String>,
            params: ParamsMap
    ): String {
        val machines = input.map(this::parseMachine)
        return machines.sumOf { simulateMachineJoltage(it) }.toString()
    }
}