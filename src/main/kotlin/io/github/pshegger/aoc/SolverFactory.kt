package io.github.pshegger.aoc

import io.github.pshegger.aoc.common.model.BaseSolver
import java.util.*
import kotlin.reflect.full.primaryConstructor

object SolverFactory {
    private const val START_YEAR = 2015

    private val solvers = mutableMapOf<Int, MutableList<BaseSolver>>()

    val latest: BaseSolver
        get() {
            val year = solvers.maxByOrNull { it.key } ?: error("There are no registered solvers")
            return year.value.maxByOrNull { it.day }
                ?: error("There are no registered solvers for year ${year.key}")
        }

    fun registerSolvers() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        for (year in START_YEAR..currentYear) {
            for (day in 1..25) {
                try {
                    (Class.forName("io.github.pshegger.aoc.y$year.Y${year}D$day")
                        .kotlin
                        .primaryConstructor
                        ?.call()
                            as? BaseSolver)
                        ?.let { registerSolver(it) }
                } catch (_: ClassNotFoundException) {
                }
            }
        }
    }

    fun getSolver(year: Int, day: Int): BaseSolver? = solvers[year]?.find { it.day == day }

    private fun registerSolver(solver: BaseSolver) {
        val yearSolvers = solvers[solver.year] ?: mutableListOf()
        yearSolvers.add(solver)
        solvers[solver.year] = yearSolvers
    }
}
