package io.github.pshegger.aoc.common

fun <T> Iterable<T>.updated(index: Int, newValue: T) =
    mapIndexed { i, t -> if (i == index) newValue else t }

fun Iterable<String>.splitByBlank(): List<List<String>> =
    fold(listOf(emptyList())) { acc, str ->
        if (str.isBlank()) {
            acc + listOf(emptyList())
        } else {
            acc.dropLast(1) + listOf(acc.last() + str)
        }
    }
