package me.salmonmoses.days.twentyfour

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import me.salmonmoses.geometry.Vector
import org.koin.core.annotation.Single
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.*
import javax.imageio.ImageIO

@Single
@Day(14, 2024)
class Day14 : DayTask {
    private data class Robot(var position: Vector, val velocity: Vector)

    override val spec1: TaskSpec
        get() = TaskSpec(
                "p=0,4 v=3,-3\n" +
                        "p=6,3 v=-1,-3\n" +
                        "p=10,3 v=-1,2\n" +
                        "p=2,0 v=2,-1\n" +
                        "p=0,0 v=1,3\n" +
                        "p=3,0 v=-2,-2\n" +
                        "p=7,6 v=-1,-3\n" +
                        "p=3,0 v=-1,-2\n" +
                        "p=9,3 v=2,3\n" +
                        "p=7,3 v=-1,2\n" +
                        "p=2,4 v=2,-3\n" +
                        "p=9,5 v=-3,-3", "12"
        )
    override val spec2: TaskSpec?
        get() = null

    private val robotPattern = Regex("p=(-?[0-9]+),(-?[0-9]+) v=(-?[0-9]+),(-?[0-9]+)")

    private fun renderRobots(robots: List<Robot>, width: Int, height: Int, outputStream: OutputStream) {
        Dimension(width, height)
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val robotsByPosition = robots.groupBy { it.position }
        (0..<height).forEach { y ->
            (0..<width).forEach { x ->
                val robotsNumber = robotsByPosition[Vector(x, y)]?.size?.let { 0xffffff } ?: 0
                img.setRGB(x, y, robotsNumber)
            }
        }
        ImageIO.write(img, "bmp", outputStream)
    }

    private fun calculateSafetyFactor(robots: List<Robot>, width: Int, height: Int): Long {
        val halfHeight = height / 2
        val halfWidth = width / 2
        val quadrants = MutableList(4, { 0L })
        robots.forEach { (position, _) ->
            val upper = position.y < halfHeight
            val lower = position.y > halfHeight
            val left = position.x < halfWidth
            val right = position.x > halfWidth
            when {
                upper && left -> quadrants[0]++
                upper && right -> quadrants[1]++
                lower && right -> quadrants[2]++
                lower && left -> quadrants[3]++
            }
        }
        return quadrants.reduce(Long::times)
    }

    override fun task1(input: List<String>, params: ParamsMap): String {
        var width = 0
        var height = 0
        val robots = robotPattern.findAll(input.joinToString("\n")).map {
            val startingPosition = Vector(it.groupValues[1].toInt(), it.groupValues[2].toInt())
            val velocity = Vector(it.groupValues[3].toInt(), it.groupValues[4].toInt())
            if (startingPosition.x > width) {
                width = startingPosition.x
            }
            if (startingPosition.y > height) {
                height = startingPosition.y
            }
            Robot(startingPosition, velocity)
        }.toList()
        repeat(100) {
            robots.forEach {
                it.position = Vector(
                        (it.position.x + it.velocity.x + width + 1) % (width + 1),
                        (it.position.y + it.velocity.y + height + 1) % (height + 1)
                )
            }
        }
        return calculateSafetyFactor(robots, width, height).toString()
    }

    override fun task2(input: List<String>, params: ParamsMap): String {
        var width = 0
        var height = 0
        val robots = robotPattern.findAll(input.joinToString("\n")).map {
            val startingPosition = Vector(it.groupValues[1].toInt(), it.groupValues[2].toInt())
            val velocity = Vector(it.groupValues[3].toInt(), it.groupValues[4].toInt())
            if (startingPosition.x > width) {
                width = startingPosition.x
            }
            if (startingPosition.y > height) {
                height = startingPosition.y
            }
            Robot(startingPosition, velocity)
        }.toList()
        repeat(100000) { step ->
            robots.forEach {
                it.position = Vector(
                        (it.position.x + it.velocity.x + width + 1) % (width + 1),
                        (it.position.y + it.velocity.y + height + 1) % (height + 1)
                )
            }
            FileOutputStream("output/day14/step${step + 1}.bmp").use { renderRobots(robots, width, height, it) }
        }
        return ""
    }
}