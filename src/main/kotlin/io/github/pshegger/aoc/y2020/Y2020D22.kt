package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.model.BaseSolver

class Y2020D22 : BaseSolver() {
    override val year = 2020
    override val day = 22

    override fun part1(): Long {
        val input = parseInput()
        val p1 = input.first.toMutableList()
        val p2 = input.second.toMutableList()

        while (p1.isNotEmpty() && p2.isNotEmpty()) {
            val c1 = p1.removeAt(0)
            val c2 = p2.removeAt(0)
            if (c1 > c2) {
                p1.add(c1)
                p1.add(c2)
            }
            if (c2 > c1) {
                p2.add(c2)
                p2.add(c1)
            }
        }
        val winnerDeck = if (p1.isNotEmpty()) p1 else p2

        return winnerDeck.foldIndexed(0L) { index, acc, card ->
            val weight = winnerDeck.size - index
            acc + weight * card
        }
    }

    override fun part2(): Long {
        val (p1, p2) = parseInput()
        val (_, winnerDeck) = recursiveCombat(p1, p2)

        return winnerDeck.foldIndexed(0L) { index, acc, card ->
            val weight = winnerDeck.size - index
            acc + weight * card
        }
    }

    private fun recursiveCombat(p1Start: List<Int>, p2Start: List<Int>): Pair<Boolean, List<Int>> {
        val rounds = mutableMapOf<Pair<List<Int>, List<Int>>, Boolean>()
        val p1 = p1Start.toMutableList()
        val p2 = p2Start.toMutableList()
        while (p1.isNotEmpty() && p2.isNotEmpty()) {
            val state = Pair(p1, p2)
            if (rounds[state] == true) {
                return Pair(true, p1)
            } else {
                rounds[state] = true
            }
            val c1 = p1.removeAt(0)
            val c2 = p2.removeAt(0)
            val (p1Wins, _) = when {
                p1.size >= c1 && p2.size >= c2 -> recursiveCombat(p1.take(c1), p2.take(c2))
                c1 > c2 -> Pair(true, p1)
                else -> Pair(false, p2)
            }

            if (p1Wins) {
                p1.add(c1)
                p1.add(c2)
            } else {
                p2.add(c2)
                p2.add(c1)
            }
        }

        return if (p1.isNotEmpty()) Pair(true, p1) else Pair(false, p2)
    }

    private fun parseInput() = readInput {
        val lines = readLines()
        val player1Cards = lines.takeWhile { it.isNotBlank() }.drop(1).map { it.toInt() }
        val player2Cards = lines.dropWhile { it.isNotBlank() }.drop(2).map { it.toInt() }
        Pair(player1Cards, player2Cards)
    }
}
