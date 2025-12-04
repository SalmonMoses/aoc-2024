package me.salmonmoses.utils

import java.util.function.Predicate

object Predicates {
    fun isTrue(): Predicate<Boolean> = Predicate { it }
}