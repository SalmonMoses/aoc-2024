package me.salmonmoses

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.TaskSpec
import me.salmonmoses.utils.Colored
import me.salmonmoses.utils.OutputColor
import okhttp3.OkHttpClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import java.util.*
import kotlin.reflect.full.findAnnotation

@Single
class TaskRunner(tasks: List<DayTask>, val inputService: InputService) {
    private val days: Map<Int, DayTask> = tasks.map { task ->
        task::class.findAnnotation<Day>()?.let {
            val day = it.day
            val year = it.year
            return@map (year * 100 + day) to task
        }
    }.filterNotNull().toMap()

    fun runDay(day: Int, year: Int) {
        if (!inputService.checkDayInput(day, year)) {
            println(Colored.foreground("Downloading input for day $day...", OutputColor.BLUE))
            inputService.downloadDayInput(day, year)
            println(Colored.foreground("Downloaded input", OutputColor.BLUE))
        } else {
            println(Colored.foreground("Input for day $day already exists, skipping downloading it", OutputColor.BLUE))
        }

        val dayIndex = year * 100 + day
        days[dayIndex]?.let {
            it.spec1?.let { spec ->
                checkSpec(it, DayTask::task1, spec, "task 1")
            }

            it.spec2?.let { spec ->
                checkSpec(it, DayTask::task2, spec, "task 2")
            }

            val input = inputService.getDayInput(day, year)
            check(input.isNotEmpty(), { "Input is empty!" })
            runTask(it, input, DayTask::task1, "task 1")
            runTask(it, input, DayTask::task2, "task 2")
        } ?: run {
            println("Task for day $day is not found!")
        }
    }

    fun runToday() {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_MONTH)
        val year = calendar.get(Calendar.YEAR)
        runDay(today, year)
    }

    fun runAll(year: Int, excluded: Set<Int> = setOf()) {
        (1..25).filter { it !in excluded }.forEach { day ->
            println(Colored.backgroundForeground("   DAY $day   ", OutputColor.YELLOW, OutputColor.BLACK))
            runDay(day, year)
            println()
        }
    }

    private fun checkSpec(day: DayTask, solver: DayTask.(List<String>) -> String, spec: TaskSpec, title: String) {
        val time = System.currentTimeMillis()
        val actual = day.solver(spec.input.split("\n"))
        val finishTime = System.currentTimeMillis() - time
        val failed = actual != spec.expectedResult
        print(
            Colored.backgroundForeground(
                " ${title.uppercase()} ",
                if (failed) OutputColor.RED else OutputColor.GREEN,
                OutputColor.BLACK,
            )
        )
        if (failed) {
            println(
                Colored.foreground(
                    " $actual != ${spec.expectedResult} [${finishTime} ms]",
                    OutputColor.RED
                )
            )
        } else {
            println(
                Colored.foreground(
                    " $actual == ${spec.expectedResult} [${finishTime} ms]",
                    OutputColor.GREEN
                )
            )
        }
    }

    private fun runTask(day: DayTask, input: List<String>, task: DayTask.(List<String>) -> String, title: String) {
        val time = System.currentTimeMillis()
        val solution = day.task(input)
        val finishTime = System.currentTimeMillis() - time
        print(Colored.backgroundForeground(" ${title.uppercase()} ", OutputColor.BLUE, OutputColor.BLACK))
        println(" Solution = $solution [${finishTime} ms]")
    }
}

@Module
@ComponentScan
class TaskRunnerModule {
    @Single
    fun httpClient(): OkHttpClient = OkHttpClient.Builder().build()
}