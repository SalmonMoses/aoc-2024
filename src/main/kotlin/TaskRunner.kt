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
            return@map day to task
        }
    }.filterNotNull().toMap()

    fun runDay(day: Int) {
        if (!inputService.checkDayInput(day)) {
            println(Colored.foreground("Downloading input for day $day...", OutputColor.BLUE))
            inputService.downloadDayInput(day)
            println(Colored.foreground("Downloaded input", OutputColor.BLUE))
        } else {
            println(Colored.foreground("Input for day $day already exists, skipping downloading it", OutputColor.BLUE))
        }

        days[day]?.let {
            checkSpec(it, DayTask::task1, it.spec1, "task 1")

            it.spec2?.let { spec ->
                checkSpec(it, DayTask::task2, spec, "task 2")
            }

            val input = inputService.getDayInput(day)
            check(input.isNotEmpty(), { "Input is empty!" })
            runTask(it, input, DayTask::task1, "task 1")
            runTask(it, input, DayTask::task2, "task 2")
        } ?: run {
            println("Task for day $day is not found!")
        }
    }

    fun runToday() {
        val today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        runDay(today)
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