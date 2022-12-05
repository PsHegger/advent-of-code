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

fun <T> Iterable<Iterable<T>>.findCommons() = map { it.toSet() }
    .reduce { a, b -> a.intersect(b) }

fun <T> Iterable<T>.permutations(): List<List<T>> {
    fun go(ls: List<T>): List<List<T>> {
        return when (ls.size) {
            0, 1 -> listOf(ls)
            else -> ls.flatMap { item -> go(ls - item).map { listOf(item) + it } }
        }
    }

    return go(this.toList())
}

fun <R> Iterable<String>.extractAll(extractor: RegExtractor, block: (List<String>) -> R): List<R> =
    mapNotNull { extractor.extract(it, block) }
