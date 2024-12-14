package io.github.pshegger.aoc.benchmark

import io.github.pshegger.aoc.common.extensions.log10
import kotlin.time.measureTime

class DigitCountBenchmark : Benchmark() {
    override val name = "Counting Digits"
    override val cases = listOf(
        Case("toString", ::countWithToStringForSmallNumber),
        Case("log", ::countWithLogForSmallNumber),
    )

    private fun countWithToStringForSmallNumber() = measureTime {
        repeat(ITERATION_COUNT) {
            it.toString().length
        }
    }

    private fun countWithLogForSmallNumber() = measureTime {
        repeat(ITERATION_COUNT) {
            it.log10() + 1
        }
    }

    companion object {
        private const val ITERATION_COUNT = 10_000_000
    }
}