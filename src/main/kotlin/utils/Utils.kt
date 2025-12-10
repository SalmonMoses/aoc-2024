package me.salmonmoses.utils

import kotlin.math.absoluteValue
import kotlin.math.roundToLong

fun <S, T> Iterable<S>.cartesianProduct(other: Iterable<T>) = this.flatMap { thisIt ->
    other.map { otherIt ->
        thisIt to otherIt
    }
}

fun <T> List<T>.cartesianProductWithoutRepeats(): Iterable<Pair<T, T>> = sequence<Pair<T, T>> {
    for (i in 0..<(size - 1)) {
        for (j in (i + 1)..<size) {
            yield(Pair(get(i), get(j)))
        }
    }
}.asIterable()

fun <S, T> Iterable<S>.steppedReduce(initial: T, operation: (T, S) -> T): Iterable<T> {
    var current = initial
    return this.map {
        current = operation(current, it)
        current
    }
}

fun Double.isInteger(): Boolean = (this - roundToLong()).absoluteValue < 0.000001

fun Int.factorial(): Long {
    require(0 <= this) { "Factorial is undefined for negative numbers." }
    // Not using BigDecimal, so we must fit inside a Long.
    require(this < 21) { "Factorial is undefined for numbers greater than 20." }
    return when (this) {
        0, 1 -> 1L
        else -> (2..this).fold(1L) { acc, i -> acc * i }
    }
}

/**
 * Iterates the permutations of the receiver array.
 * By using an iterator, we minimize the memory footprint.
 */
fun <T> List<T>.permutations(): Iterator<List<T>> {
    // The nth permutation of the receiver list.
    fun <T> List<T>.permutation(nth: Long): List<T> {
        if (isEmpty()) return emptyList()
        val index = (nth % size)
                .also { require(it < Int.MAX_VALUE) }
                .toInt()
        // Grab the first element...
        val head = elementAt(index)
        // ...make a list of what's left...
        val tail = slice(indices.filter { it != index })
        // ...permute it...
        val tailPerm = tail.permutation(nth / size)
        // ...jam it all together.
        return listOf(head) + tailPerm
    }

    val total = size.factorial()
    return object : Iterator<List<T>> {
        var current = 0L
        override fun hasNext(): Boolean = current < total

        override fun next(): List<T> {
            require(hasNext()) { "No more permutations." }
            return this@permutations.permutation(current++)
        }
    }
}

fun <T> equalElementWise(a: List<T>, b: List<T>): Boolean {
    if (a.size != b.size) {
        return false
    }
    for (i in a.indices) {
        if (a[i] != b[i]) {
            return false
        }
    }
    return true
}

fun rangesOverlap(x: IntRange, y: IntRange): Boolean = x.last >= y.first && x.first <= y.last
fun rangesOverlap(x: LongRange, y: LongRange): Boolean = x.last >= y.first && x.first <= y.last
