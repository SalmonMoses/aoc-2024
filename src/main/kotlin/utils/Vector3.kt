package me.salmonmoses.utils

import kotlin.math.absoluteValue
import kotlin.math.pow

data class Vector3(val x: Int, val y: Int, val z: Int) {
    operator fun plus(delta: Vector3): Vector3 {
        return Vector3(x + delta.x, y + delta.y, z + delta.z)
    }

    operator fun minus(delta: Vector3): Vector3 {
        return Vector3(x - delta.x, y - delta.y, z - delta.z)
    }

    operator fun times(k: Int): Vector3 {
        return Vector3(x * k, y * k, z * k)
    }

    override operator fun equals(other: Any?): Boolean {
        return other is Vector3 && x == other.x && y == other.y && z == other.z
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    fun sqrtLength(): Double {
        return x.toDouble().pow(2.0) + y.toDouble().pow(2.0) + z.toDouble().pow(2.0)
    }

    fun sqrtEuclidean(other: Vector3): Double {
        return (this - other).sqrtLength()
    }

    fun manhattan(other: Vector3): Int {
        return (x - other.x).absoluteValue + (y - other.y).absoluteValue
    }
}