package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.model.BaseSolver

class Y2015D16 : BaseSolver() {
    override val year = 2015
    override val day = 16

    private val equalityChecker: AnalysisResult.(Int) -> Boolean = { value == it }
    private val greaterChecker: AnalysisResult.(Int) -> Boolean = { it > value }
    private val fewerChecker: AnalysisResult.(Int) -> Boolean = { it < value }

    private val analysisResults = listOf(
        AnalysisResult("children", 3, equalityChecker),
        AnalysisResult("cats", 7, greaterChecker),
        AnalysisResult("samoyeds", 2, equalityChecker),
        AnalysisResult("pomeranians", 3, fewerChecker),
        AnalysisResult("akitas", 0, equalityChecker),
        AnalysisResult("vizslas", 0, equalityChecker),
        AnalysisResult("goldfish", 5, fewerChecker),
        AnalysisResult("trees", 3, greaterChecker),
        AnalysisResult("cars", 2, equalityChecker),
        AnalysisResult("perfumes", 1, equalityChecker),
    )

    override fun part1(): Int = solve(false).first().number
    override fun part2(): Int = solve(true).first().number

    private fun solve(useChecker: Boolean) = analysisResults.fold(parseInput()) { remainingSues, result ->
        remainingSues.filter { sue ->
            val owned = sue.compounds[result.name]
            if (useChecker) {
                owned == null || result.checker(result, owned)
            } else {
                owned == null || owned == result.value
            }
        }
    }

    private fun parseInput() = readInput {
        readLines().map { line ->
            val (nameAndNumber, compoundsStr) = line.split(":", limit = 2)
            val number = nameAndNumber.split(" ")[1].toInt()
            val compounds = compoundsStr.split(",")
            Sue(
                number,
                compounds.associate { compound ->
                    val (name, value) = compound.split(":")
                    Pair(name.trim(), value.trim().toInt())
                }
            )
        }
    }

    private data class AnalysisResult(val name: String, val value: Int, val checker: AnalysisResult.(Int) -> Boolean)
    private data class Sue(val number: Int, val compounds: Map<String, Int>)
}
