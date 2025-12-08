package me.salmonmoses.days.twentyfour

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import org.koin.core.annotation.Single

@Single
@Day(9, 2024)
class Day9 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec("2333133121414131402", "1928")
    override val spec2: TaskSpec
        get() = TaskSpec("2333133121414131402", "2858")

    override fun task1(input: List<String>, params: ParamsMap): String {
        val filesystemDescriptor = input[0].split("").filter(String::isNotEmpty).map(String::toInt)
        val filesystem = mutableListOf<Int>()
        var currentFileId = 0
        filesystemDescriptor.forEachIndexed { index, number ->
            val isFile = index % 2 == 0
            if (isFile) {
                for (i in 0..<number) {
                    filesystem.add(currentFileId)
                }
                currentFileId++
            } else {
                for (i in 0..<number) {
                    filesystem.add(-1)
                }
            }
        }
        var backPointer = filesystem.size - 1
        var frontPointer = 0
        while (frontPointer < backPointer) {
            if (filesystem[frontPointer] == -1) {
                filesystem[frontPointer] = filesystem[backPointer]
                filesystem[backPointer] = -1
                while (filesystem[backPointer] == -1) {
                    backPointer--
                }
            }
            frontPointer++
        }
        return filesystem.map(Int::toLong).withIndex()
                .sumOf { file -> file.index * file.value.coerceAtLeast(0) }
                .toString()
    }

    override fun task2(input: List<String>, params: ParamsMap): String {
        val filesystemDescriptor = input[0].split("").filter(String::isNotEmpty).map(String::toInt)
        val filesystem = mutableListOf<Pair<Int, Int>>()
        var currentFileId = 0
        filesystemDescriptor.forEachIndexed { index, number ->
            val isFile = index % 2 == 0
            if (isFile) {
                filesystem.add(Pair(currentFileId, number))
                currentFileId++
            } else {
                filesystem.add(Pair(-1, number))
            }
        }
        for (index in 0..<filesystem.size) {
            val file = filesystem[index]
            if (file.first == -1) {
                for (backIndex in filesystem.size - 1 downTo index) {
                    if (filesystem[backIndex].first != -1 && filesystem[backIndex].second <= file.second) {
                        val remainedSize = file.second - filesystem[backIndex].second
                        filesystem[index] = filesystem[backIndex]
                        filesystem[backIndex] = Pair(-1, filesystem[backIndex].second)
                        if (remainedSize > 0) {
                            filesystem.add(index + 1, Pair(-1, remainedSize))
                        }
                        break
                    }
                }
            }
        }
        val fullFilesystem = mutableListOf<Int>()
        filesystem.forEach { (fileId, size) ->
            for (i in 0..<size) {
                fullFilesystem.add(fileId)
            }
        }
        return fullFilesystem
                .map(Int::toLong)
                .withIndex()
                .sumOf { file -> file.index * file.value.coerceAtLeast(0) }
                .toString()
    }
}