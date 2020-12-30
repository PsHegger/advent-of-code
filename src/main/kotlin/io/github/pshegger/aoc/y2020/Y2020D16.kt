package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.BaseSolver

class Y2020D16 : BaseSolver() {
    override val year = 2020
    override val day = 16

    override fun part1(): Int = parseInput().let { input ->
        input.nearby.mapNotNull { it.invalidField(input.validators) }.sum()
    }

    override fun part2(): Long = parseInput().withoutInvalidNearby().let { input ->
        val possibleIndices = input.validators.associateWith { validator ->
            input.own.fields.indices.filter { fieldI ->
                input.nearby.all { ticket -> validator.isFieldValid(ticket.fields[fieldI]) }
            }
        }
        val (solvedInput, _) = input.validators.fold(Pair(input, emptyList<Int>())) { (input, alreadyFound), _ ->
            val single = possibleIndices.entries.first { (_, v) -> (v - alreadyFound).size == 1 }.key
            val possibility = ((possibleIndices[single] ?: error("WTF?!")) - alreadyFound)[0]
            Pair(input.copyWithValidatorFieldIndex(single, possibility), alreadyFound + possibility)
        }
        solvedInput.validators
            .filter { it.name.startsWith("departure") }
            .fold(1L) { acc, validator ->
                acc * solvedInput.own.fields[validator.fieldIndex]
            }
    }

    private fun parseInput() =
        readInput {
            readLines().fold(Pair(0, Input.empty)) { (mode, input), line ->
                if (line.isBlank()) {
                    Pair(mode + 1, input)
                } else {
                    when (mode) {
                        0 -> Pair(mode, input.copy(validators = input.validators + Validator.parseString(line)))
                        2 -> Pair(mode, input.copy(own = Ticket.parseString(line)))
                        4 -> Pair(mode, input.copy(nearby = input.nearby + Ticket.parseString(line)))
                        1, 3 -> Pair(mode + 1, input)
                        else -> Pair(mode, input)
                    }
                }
            }.second
        }

    private data class Input(val validators: List<Validator>, val own: Ticket, val nearby: List<Ticket>) {

        fun copyWithValidatorFieldIndex(validator: Validator, fieldIndex: Int) = copy(
            validators = validators.filterNot { it == validator } + validator.copy(fieldIndex = fieldIndex)
        )

        fun withoutInvalidNearby() = copy(
            nearby = nearby.filter { it.invalidField(validators) == null }
        )

        companion object {
            val empty = Input(emptyList(), Ticket(emptyList()), emptyList())
        }
    }

    private data class Validator(val name: String, val ranges: List<IntRange>, val fieldIndex: Int = -1) {
        fun isFieldValid(field: Int) = ranges.any { field in it }

        companion object {
            fun parseString(str: String) = validatorRegex.matchEntire(str)?.let { res ->
                Validator(
                    res.groupValues[1],
                    listOf(
                        IntRange(res.groupValues[2].toInt(), res.groupValues[3].toInt()),
                        IntRange(res.groupValues[4].toInt(), res.groupValues[5].toInt()),
                    )
                )
            } ?: error("Invalid validator line")

            private val validatorRegex = "(.+): (\\d+)-(\\d+) or (\\d+)-(\\d+)".toRegex()
        }
    }

    private data class Ticket(val fields: List<Int>) {

        fun invalidField(validators: List<Validator>) =
            fields.firstOrNull { field -> validators.none { it.isFieldValid(field) } }

        companion object {
            fun parseString(str: String) = Ticket(str.trim().split(",").map { it.toInt() })
        }
    }
}
