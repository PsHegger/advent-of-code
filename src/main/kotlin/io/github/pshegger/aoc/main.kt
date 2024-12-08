package io.github.pshegger.aoc

import io.github.pshegger.aoc.common.model.Answer
import kotlin.time.measureTime

fun main(args: Array<String>) {
    SolverFactory.registerSolvers()

    val config = ArgsParser(args).parse()
    val solver = if (config.year == null || config.day == null) {
        SolverFactory.latest
    } else {
        SolverFactory.getSolver(config.year, config.day)
    }

    if (solver == null) {
        println("Solver not found for year ${config.year} and day ${config.day}")
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
