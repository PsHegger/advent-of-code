package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.model.BaseSolver

class Y2020D25 : BaseSolver() {
    override val year = 2020
    override val day = 25

    override fun part1(): Long = parseInput().let { (cardPublicKey, doorPublicKey) ->
        calculateKey(findLoopSize(cardPublicKey), doorPublicKey)
    }

    private fun parseInput() = readInput {
        val (cardPublicKey, doorPublicKey) = readLines().map { it.trim().toInt() }
        Pair(cardPublicKey, doorPublicKey)
    }

    private fun calculateKey(loopSize: Int, subjectNumber: Int): Long {
        var value = 1L
        repeat(loopSize) {
            value = (value * subjectNumber) % 20201227
        }
        return value
    }

    private fun findLoopSize(key: Int, subjectNumber: Int = 7): Int {
        var ctr = 0
        var value = 1L
        while (value != key.toLong()) {
            ctr++
            value = (value * subjectNumber) % 20201227
        }
        return ctr
    }
}
