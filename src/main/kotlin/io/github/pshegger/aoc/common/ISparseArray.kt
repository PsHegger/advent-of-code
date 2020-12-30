package io.github.pshegger.aoc.common

interface ISparseArray<T> {
    operator fun get(index: Int): T?
    operator fun contains(index: Int): Boolean
    fun getOrElse(index: Int, default: T): T
}
