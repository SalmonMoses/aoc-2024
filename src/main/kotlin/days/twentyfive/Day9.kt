package me.salmonmoses.days.twentyfive

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.ParamsMap
import me.salmonmoses.days.TaskSpec
import me.salmonmoses.geometry.Box
import me.salmonmoses.geometry.Polygon
import me.salmonmoses.geometry.Vector
import me.salmonmoses.geometry.VirtualGrid
import me.salmonmoses.utils.cartesianProductWithoutRepeats
import org.koin.core.annotation.Single
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.FileOutputStream
import java.io.OutputStream
import javax.imageio.ImageIO

@Single
@Day(9, 2025)
class Day9 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
                "7,1\n" +
                        "11,1\n" +
                        "11,7\n" +
                        "9,7\n" +
                        "9,5\n" +
                        "2,5\n" +
                        "2,3\n" +
                        "7,3", "50"
        )
    override val spec2: TaskSpec?
        get() = TaskSpec(
                "7,1\n" +
                        "11,1\n" +
                        "11,7\n" +
                        "9,7\n" +
                        "9,5\n" +
                        "2,5\n" +
                        "2,3\n" +
                        "7,3", "24"
        )

    override fun task1(
            input: List<String>,
            params: ParamsMap
    ): String {
        val redTiles = input.map {
            val coords = it.split(",")
            Vector(coords[0].toInt(), coords[1].toInt())
        }

        return redTiles.cartesianProductWithoutRepeats()
                .maxOf { Box(it).area }
                .toString()
    }

    override fun task2(
            input: List<String>,
            params: ParamsMap
    ): String {
        val redTiles = input.map {
            val coords = it.split(",")
            Vector(coords[0].toInt(), coords[1].toInt())
        }

        val polygon = Polygon(redTiles)

        return redTiles.cartesianProductWithoutRepeats()
                .map(::Box)
                .sortedByDescending(Box::area)
                .first { !polygon.intersects(it) }.area.toString()
    }
}