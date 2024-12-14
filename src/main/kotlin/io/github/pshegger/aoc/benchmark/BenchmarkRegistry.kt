package io.github.pshegger.aoc.benchmark

import kotlin.reflect.full.primaryConstructor

object BenchmarkRegistry {

    private val benchmarks = mutableListOf<Benchmark>()

    fun registerBenchmarks() {
        Benchmark::class.sealedSubclasses.forEach { subClass ->
            subClass.primaryConstructor
                ?.call()
                ?.let { benchmarks.add(it) }
        }
    }

    fun runAllBenchmarks() {
        benchmarks.forEach { it.run() }
    }
}