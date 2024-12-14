package io.github.pshegger.aoc

import io.github.pshegger.aoc.benchmark.BenchmarkRegistry
import io.github.pshegger.aoc.common.model.Answer
import kotlin.time.measureTime

fun main(args: Array<String>) {
    val config = ArgsParser(args).parse()
    when (config.mode) {
        Mode.Solver -> solveTask(config.year, config.day)
        Mode.Benchmark -> runBenchmarks()
    }
}

private fun solveTask(year: Int?, day: Int?) {
    SolverFactory.registerSolvers()

    val solver = if (year == null || day == null) {
        SolverFactory.latest
    } else {
        SolverFactory.getSolver(year, day)
    }

    if (solver == null) {
        println("Solver not found for year $year and day $day")
        return
    }

    val solution: Answer
    val duration = measureTime {
        solution = solver.solve()
    }

    println("Solution for ${solver.year} #${solver.day}")
    println(solution)
    println("Found solution in $duration")
}

private fun runBenchmarks() {
    BenchmarkRegistry.registerBenchmarks()
    BenchmarkRegistry.runAllBenchmarks()
}