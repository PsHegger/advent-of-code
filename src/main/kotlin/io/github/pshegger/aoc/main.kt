package io.github.pshegger.aoc

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

    val start = System.currentTimeMillis()
    val solution = solver.solve()
    val duration = System.currentTimeMillis() - start

    println("Solution for ${solver.year} #${solver.day}")
    println(solution)
    println("Found solution in ${String.format("%.3f", duration / 1000.0)} s")
}
