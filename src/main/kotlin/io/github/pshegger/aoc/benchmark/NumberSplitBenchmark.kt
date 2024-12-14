package io.github.pshegger.aoc.benchmark

import io.github.pshegger.aoc.common.extensions.log10
import io.github.pshegger.aoc.common.extensions.pow
import kotlin.time.measureTime

class NumberSplitBenchmark : Benchmark() {
    override val name = "Split Number in Half (by Digits)"
    override val cases = listOf(
        Case("toString", ::splitWithToString),
        Case("maths", ::splitWithLog),
    )

    private fun splitWithToString() = measureTime {
        repeat(ITERATION_COUNT) {
            if (it.canSplit) {
                val str = it.toString()
                Pair(str.take(str.length / 2).toInt(), str.drop(str.length / 2).toInt())
            }
        }
    }

    private fun splitWithLog() = measureTime {
        repeat(ITERATION_COUNT) {
            if (it.canSplit) {
                val x = 10.pow(it.log10() / 2 + 1)
                Pair(it / x, it % x)
            }
        }
    }

    private val Int.canSplit: Boolean get() = toString().length % 2 == 0

    companion object {
        private const val ITERATION_COUNT = 10_000_000
    }
}