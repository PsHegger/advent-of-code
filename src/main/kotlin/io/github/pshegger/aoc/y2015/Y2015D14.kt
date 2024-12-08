package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.extensions.extractAll
import io.github.pshegger.aoc.common.extensions.toExtractor

class Y2015D14 : BaseSolver() {
    override val year = 2015
    override val day = 14

    private val extractor = "%s can fly %d km/s for %d seconds, but then must rest for %d seconds.".toExtractor()

    private val reindeer by lazy { parseInput() }

    override fun part1(): Int = reindeer.maxOf { it.distanceAfter(RACE_DURATION) }

    override fun part2(): Any = (1..RACE_DURATION).fold(
        reindeer.associate { it.name to 0 }
    ) { scores, seconds ->
        val maxDistance = reindeer.maxOf { it.distanceAfter(seconds) }
        val leaders = reindeer.filter { it.distanceAfter(seconds) == maxDistance }.map { it.name }
        scores + scores.entries
            .filter { it.key in leaders }
            .map { Pair(it.key, it.value + 1) }
    }.maxOf { it.value }

    private fun parseInput() = readInput {
        readLines()
            .extractAll(extractor) { (name, speed, flyDuration, restDuration) ->
                Reindeer(
                    name,
                    speed.toInt(),
                    flyDuration.toInt(),
                    restDuration.toInt()
                )
            }
    }

    private data class Reindeer(val name: String, val speed: Int, val flyDuration: Int, val restDuration: Int) {

        private val cycleDuration = flyDuration + restDuration

        fun distanceAfter(seconds: Int): Int {
            val fullCycleDistance = seconds / cycleDuration * speed * flyDuration
            val remainingTime = seconds % cycleDuration
            val remainingDistance = if (remainingTime < flyDuration) {
                speed * remainingTime
            } else {
                speed * flyDuration
            }
            return fullCycleDistance + remainingDistance
        }
    }

    companion object {
        private const val RACE_DURATION = 2503
    }
}
