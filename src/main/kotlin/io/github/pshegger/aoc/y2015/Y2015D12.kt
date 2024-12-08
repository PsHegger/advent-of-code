package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.model.BaseSolver

class Y2015D12 : BaseSolver() {
    override val year = 2015
    override val day = 12

    private val tokenizedInput: Token by lazy { tokenize(parseInput()) }

    override fun part1(): Int = tokenizedInput.sumNumbers(false)
    override fun part2(): Int = tokenizedInput.sumNumbers(true)

    private fun Token.sumNumbers(ignoreRedObjects: Boolean): Int = when (this) {
        is Token.Array -> items.sumOf { it.sumNumbers(ignoreRedObjects) }
        is Token.NumberLiteral -> value
        is Token.Object -> {
            val hasRed = members.any { (_, token) -> (token as? Token.StringLiteral)?.value == "red" }
            if (ignoreRedObjects && hasRed) {
                0
            } else {
                members.entries.sumOf { it.value.sumNumbers(ignoreRedObjects) }
            }
        }
        is Token.StringLiteral -> 0
    }

    private fun tokenize(data: String): Token = when (data.first()) {
        '"' -> Token.StringLiteral(data.drop(1).dropLast(1))
        '[' -> {
            val parts = smartSplit(data.drop(1).dropLast(1))
            Token.Array(parts.map { tokenize(it) })
        }
        '{' -> {
            val parts = smartSplit(data.drop(1).dropLast(1))
            Token.Object(parts.associate { str ->
                val (key, value) = str.split(':', limit = 2)
                Pair(key, tokenize(value))
            })
        }
        else -> Token.NumberLiteral(data.toInt())
    }

    private fun smartSplit(data: String): List<String> {
        val result = mutableListOf<String>()
        var ptr = 0
        while (ptr < data.length) {
            var ctr = 0
            var doubleQuoteSign = 1
            for (i in ptr until data.length) {
                if (i == data.length - 1) {
                    result.add(data.drop(ptr))
                    ptr = data.length
                    break
                }
                when (data[i]) {
                    '[', '{' -> ctr++
                    ']', '}' -> ctr--
                    '"' -> {
                        ctr += doubleQuoteSign
                        doubleQuoteSign *= -1
                    }
                    ',' -> {
                        if (ctr == 0) {
                            result.add(data.substring(ptr, i))
                            ptr = i + 1
                            break
                        }
                    }
                }
            }
        }
        return result
    }

    private fun parseInput() = readInput {
        readLines()[0]
    }

    private sealed class Token {
        data class Array(val items: List<Token>) : Token()
        data class Object(val members: Map<String, Token>) : Token()
        data class StringLiteral(val value: String) : Token()
        data class NumberLiteral(val value: Int) : Token()
    }
}
