package me.salmonmoses.geometry

import kotlin.math.absoluteValue
import kotlin.math.pow

data class Vector(val x: Int, val y: Int) {
    operator fun plus(delta: Vector): Vector = Vector(x + delta.x, y + delta.y)

    operator fun minus(delta: Vector): Vector = Vector(x - delta.x, y - delta.y)

    operator fun times(k: Int): Vector = Vector(x * k, y * k)

    override operator fun equals(other: Any?): Boolean = other is Vector && x == other.x && y == other.y

    override fun hashCode(): Int = javaClass.hashCode()

    fun rotateClockwise(): Vector = Vector(-y, x)

    fun sqrtLength(): Double = x.toDouble().pow(2.0) + y.toDouble().pow(2.0)

    fun sqrtDistance(other: Vector): Double = (this - other).sqrtLength()

    fun manhattan(other: Vector): Int = (x - other.x).absoluteValue + (y - other.y).absoluteValue

    fun midpoint(other: Vector): Vector = Vector((x - other.x) / 2, (y - other.y) / 2)
}