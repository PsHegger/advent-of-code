package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.BaseSolver
import io.github.pshegger.aoc.common.memoize
import kotlin.math.pow

class Y2015D11 : BaseSolver() {
    override val year = 2015
    override val day = 11

    private val consecutiveIncreasingValidator: (String) -> Boolean = { password ->
        password.indices.drop(2).any { i ->
            password[i].code == password[i - 1].code + 1 &&
                password[i].code == password[i - 2].code + 2
        }
    }

    private val mistakableLetterValidator: (String) -> Boolean = { password ->
        password.none { it in listOf('i', 'o', 'l') }
    }

    private val letterPairValidator: (String) -> Boolean = { password ->
        val letterPairs = mutableListOf<Pair<Char, Int>>()
        password.indices.drop(1).forEach { i ->
            if (password[i] == password[i - 1]) {
                letterPairs.add(Pair(password[i], i - 1))
            }
        }
        letterPairs.count { p ->
            (letterPairs - p).any { it.second != p.second - 1 && it.second != p.second + 1 }
        } >= 2
    }

    private val validators = listOf(
        consecutiveIncreasingValidator,
        mistakableLetterValidator,
        letterPairValidator
    )

    override fun part1(): String = solveForPassword(CURRENT_PASSWORD)
    override fun part2(): String = solveForPassword(part1())

    private fun solveForPassword(currentPassword: String): String {
        var password = currentPassword
        do {
            password = password.nextPassword()
        } while (!password.validate(validators))
        return password
    }

    private fun String.validate(validators: List<(String) -> Boolean>) =
        validators.all { it.invoke(this) }

    private fun String.nextPassword(): String {
        var nextPasswordCode = mapIndexed { i, c ->
            (c - 'a') * (26.0).pow(length - i - 1).toLong()
        }.sum() + 1
        val resultArray = mutableListOf<Char>()
        repeat(length) {
            resultArray.add((nextPasswordCode % 26 + 'a'.code).toInt().toChar())
            nextPasswordCode /= 26
        }
        return resultArray.reversed().joinToString(separator = "")
    }

    companion object {

        private const val CURRENT_PASSWORD = "hxbxwxba"
    }
}
