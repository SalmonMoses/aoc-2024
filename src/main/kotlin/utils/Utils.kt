package me.salmonmoses.utils

import kotlin.math.absoluteValue
import kotlin.math.roundToLong

fun <S, T> Iterable<S>.cartesianProduct(other: Iterable<T>) = this.flatMap { thisIt ->
    other.map { otherIt ->
        thisIt to otherIt
    }
}

fun Double.isInteger(): Boolean = (this - roundToLong()).absoluteValue < 0.000001