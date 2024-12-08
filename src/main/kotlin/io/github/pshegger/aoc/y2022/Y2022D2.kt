package io.github.pshegger.aoc.y2022

import io.github.pshegger.aoc.common.model.BaseSolver

class Y2022D2 : BaseSolver() {
    override val year = 2022
    override val day = 2

    override fun part1(): Int {
        fun EncryptedSymbol.decrypt() = when (this) {
            EncryptedSymbol.X -> Symbol.Rock
            EncryptedSymbol.Y -> Symbol.Paper
            EncryptedSymbol.Z -> Symbol.Scissors
        }

        return parseInput().fold(0) { score, (enemy, me) ->
            val decrypted = me.decrypt()
            score + (resultTable[enemy]?.get(decrypted) ?: 0) + decrypted.pointValue
        }
    }

    override fun part2(): Int {
        fun EncryptedSymbol.decrypt(enemy: Symbol): Symbol = when (this) {
            EncryptedSymbol.X -> resultTable[enemy]!!.entries.first { it.value == LOSE_SCORE }.key
            EncryptedSymbol.Y -> resultTable[enemy]!!.entries.first { it.value == DRAW_SCORE }.key
            EncryptedSymbol.Z -> resultTable[enemy]!!.entries.first { it.value == WIN_SCORE }.key
        }

        return parseInput().fold(0) { score, (enemy, me) ->
            val decrypted = me.decrypt(enemy)
            score + (resultTable[enemy]?.get(decrypted) ?: 0) + decrypted.pointValue
        }
    }

    private fun parseInput() = readInput {
        readLines()
            .map { line ->
                val (enemyChar, myChar) = line.split(' ', limit = 2)
                val enemySymbol = when (enemyChar) {
                    "A" -> Symbol.Rock
                    "B" -> Symbol.Paper
                    "C" -> Symbol.Scissors
                    else -> throw IllegalArgumentException("Unknown symbol: $enemyChar")
                }
                val mySymbol = when (myChar) {
                    "X" -> EncryptedSymbol.X
                    "Y" -> EncryptedSymbol.Y
                    "Z" -> EncryptedSymbol.Z
                    else -> throw IllegalArgumentException("Unknown symbol: $enemyChar")
                }
                Pair(enemySymbol, mySymbol)
            }
    }

    private enum class Symbol(val pointValue: Int) {
        Rock(1), Paper(2), Scissors(3)
    }

    private enum class EncryptedSymbol {
        X, Y, Z
    }

    companion object {
        private const val LOSE_SCORE = 0
        private const val DRAW_SCORE = 3
        private const val WIN_SCORE = 6

        private val resultTable = mapOf(
            Symbol.Rock to mapOf(
                Symbol.Rock to DRAW_SCORE,
                Symbol.Paper to WIN_SCORE,
                Symbol.Scissors to LOSE_SCORE,
            ),
            Symbol.Paper to mapOf(
                Symbol.Rock to LOSE_SCORE,
                Symbol.Paper to DRAW_SCORE,
                Symbol.Scissors to WIN_SCORE,
            ),
            Symbol.Scissors to mapOf(
                Symbol.Rock to WIN_SCORE,
                Symbol.Paper to LOSE_SCORE,
                Symbol.Scissors to DRAW_SCORE,
            ),
        )
    }
}
