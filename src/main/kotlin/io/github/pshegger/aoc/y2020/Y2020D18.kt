package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.model.BaseSolver

@ExperimentalStdlibApi
class Y2020D18 : BaseSolver() {
    override val year = 2020
    override val day = 18

    override fun part1(): Long =
        parseInput(false).fold(0L) { acc, expression ->
            acc + expression.apply()
        }

    override fun part2(): Long =
        parseInput(true).fold(0L) { acc, expression ->
            acc + expression.apply()
        }

    private fun parseInput(plusPrecedence: Boolean) = readInput {
        readLines().map { parseAsExpression(it.trim(), plusPrecedence) }
    }

    private fun parseAsExpression(str: String, plusPrecedence: Boolean): Expression {
        if (str.startsWith("(") && str.endsWith(")")) {
            var ctr = 1
            var i = 0
            while (ctr > 0) {
                i++
                if (str[i] == '(') ctr++
                if (str[i] == ')') ctr--
            }
            if (i == str.length - 1) {
                return parseAsExpression(str.drop(1).dropLast(1), plusPrecedence)
            }
        }
        val parts = buildList<String> {
            val parts = str.split(" ")
            var i = 0
            while (i < parts.size) {
                if (parts[i].startsWith("(")) {
                    val startI = i
                    var ctr = parts[i].count { it == '(' }
                    while (ctr > 0) {
                        i++
                        ctr += parts[i].takeWhile { it == '(' }.count()
                        ctr -= parts[i].takeLastWhile { it == ')' }.count()
                    }
                    add(parts.drop(startI).take(i - startI + 1).joinToString(" "))
                } else {
                    add(parts[i])
                }
                i++
            }
        }
        if (parts.size == 1) return Expression.Number(parts[0].toLong())

        if (plusPrecedence && parts.size > 3) {
            val plusIndex = parts.indexOfFirst { it == "+" }
            if (plusIndex > 0) {
                val newParts = parts.take(plusIndex - 1) +
                        "(${parts[plusIndex - 1]} + ${parts[plusIndex + 1]})" +
                        parts.drop(plusIndex + 2)
                return parseAsExpression(newParts.joinToString(" "), plusPrecedence)
            }
        }

        val lastOperatorIndex = parts.indexOfLast { it == "*" || it == "+" }

        val op1 = parts.take(lastOperatorIndex).joinToString(" ")
        val op2 = parts.drop(lastOperatorIndex + 1).joinToString(" ")

        return when (parts[lastOperatorIndex]) {
            "+" -> Expression.OpPlus(parseAsExpression(op1, plusPrecedence), parseAsExpression(op2, plusPrecedence))
            "*" -> Expression.OpTimes(parseAsExpression(op1, plusPrecedence), parseAsExpression(op2, plusPrecedence))
            else -> error("WTF: $str")
        }
    }


    private sealed class Expression {
        data class OpTimes(val op1: Expression, val op2: Expression) : Expression()
        data class OpPlus(val op1: Expression, val op2: Expression) : Expression()
        data class Number(val value: Long) : Expression()

        fun apply(): Long = when (this) {
            is OpTimes -> op1.apply() * op2.apply()
            is OpPlus -> op1.apply() + op2.apply()
            is Number -> value
        }
    }
}
