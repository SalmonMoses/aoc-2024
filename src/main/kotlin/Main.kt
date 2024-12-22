package me.salmonmoses

import org.koin.core.context.startKoin
import org.koin.fileProperties
import org.koin.ksp.generated.module

fun main() {
    val koin = startKoin {
        fileProperties()
        modules(TaskRunnerModule().module)
    }.koin

//    koin.get<TaskRunner>().runDay(6, 2024)
    koin.get<TaskRunner>().runToday()
}