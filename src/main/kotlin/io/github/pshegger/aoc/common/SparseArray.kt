package io.github.pshegger.aoc.common

data class SparseArray<T>(private val content: Map<Int, T>): ISparseArray<T> {
    constructor() : this(emptyMap())

    override fun get(index: Int): T? = content[index]
    override fun contains(index: Int): Boolean = content.containsKey(index)
    override fun getOrElse(index: Int, default: T) = content[index] ?: default
}
