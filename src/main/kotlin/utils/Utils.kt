package me.salmonmoses.utils

fun <S, T> Iterable<S>.cartesianProduct(other: Iterable<T>) = this.flatMap { thisIt ->
    other.map { otherIt ->
        thisIt to otherIt
    }
}
