package io.github.pshegger.aoc.y2015

import io.github.pshegger.aoc.common.BaseSolver

class Y2015D7 : BaseSolver() {
    override val year = 2015
    override val day = 7

    override fun part1(): Int? {
        val circuit = parseInput()
        circuit.emulate()
        return circuit["a"]
    }

    override fun part2(): Int? {
        val circuit = parseInput()
        circuit.emulate()
        val a = circuit["a"] ?: error("a is missing")
        circuit.reset()
        circuit.overrideWire("b", a)
        circuit.emulate()
        return circuit["a"]
    }

    private fun parseInput() = readInput {
        Circuit(readLines().map { Instruction.parseString(it.trim()) })
    }

    private data class Circuit(
        private val instructions: List<Instruction>,
        private val wires: MutableMap<String, Int> = emptyMap<String, Int>().toMutableMap()
    ) {
        private val remainingInstructions = instructions.indices.toMutableSet()
        private val overrides = emptyMap<String, Int>().toMutableMap()

        fun emulate() {
            while (remainingInstructions.isNotEmpty()) {
                for (instructionIndex in remainingInstructions) {
                    if (instructions[instructionIndex].apply(this)) {
                        remainingInstructions.remove(instructionIndex)
                        break
                    }
                }
            }
        }

        fun overrideWire(wireName: String, signalStrength: Int) {
            wires[wireName] = signalStrength
            overrides[wireName] = signalStrength
        }

        fun reset() {
            wires.clear()
            overrides.clear()
            remainingInstructions.addAll(instructions.indices)
        }

        operator fun get(wireName: String) = wires[wireName]
        operator fun set(wireName: String, signalStrength: Int) {
            wires[wireName] = overrides[wireName] ?: signalStrength
        }
    }

    private sealed class Operand {
        data class Signal(private val strength: Int) : Operand() {
            override fun getSignalStrength(circuit: Circuit): Int = strength
        }

        data class Wire(private val name: String) : Operand() {
            override fun getSignalStrength(circuit: Circuit): Int? = circuit[name]
        }

        abstract fun getSignalStrength(circuit: Circuit): Int?

        companion object {
            fun parseString(str: String) = str.toIntOrNull()?.let { Signal(it) } ?: Wire(str)
        }
    }

    private sealed class Instruction(protected open val resultWire: String) {

        data class Assign(private val op: Operand, override val resultWire: String) : Instruction(resultWire) {
            override fun apply(circuit: Circuit): Boolean {
                val v = op.getSignalStrength(circuit) ?: return false
                circuit[resultWire] = v
                return true
            }
        }

        data class And(private val op1: Operand, private val op2: Operand, override val resultWire: String) :
            Instruction(resultWire) {
            override fun apply(circuit: Circuit): Boolean {
                val v1 = op1.getSignalStrength(circuit) ?: return false
                val v2 = op2.getSignalStrength(circuit) ?: return false
                circuit[resultWire] = (v1 and v2) and BIT_MASK
                return true
            }
        }

        data class LShift(private val op1: Operand, private val op2: Operand, override val resultWire: String) :
            Instruction(resultWire) {
            override fun apply(circuit: Circuit): Boolean {
                val v1 = op1.getSignalStrength(circuit) ?: return false
                val v2 = op2.getSignalStrength(circuit) ?: return false
                circuit[resultWire] = (v1 shl v2) and BIT_MASK
                return true
            }
        }

        data class RShift(private val op1: Operand, private val op2: Operand, override val resultWire: String) :
            Instruction(resultWire) {
            override fun apply(circuit: Circuit): Boolean {
                val v1 = op1.getSignalStrength(circuit) ?: return false
                val v2 = op2.getSignalStrength(circuit) ?: return false
                circuit[resultWire] = (v1 shr v2) and BIT_MASK
                return true
            }
        }

        data class Or(private val op1: Operand, private val op2: Operand, override val resultWire: String) :
            Instruction(resultWire) {
            override fun apply(circuit: Circuit): Boolean {
                val v1 = op1.getSignalStrength(circuit) ?: return false
                val v2 = op2.getSignalStrength(circuit) ?: return false
                circuit[resultWire] = (v1 or v2) and BIT_MASK
                return true
            }
        }

        data class Not(private val op: Operand, override val resultWire: String) : Instruction(resultWire) {
            override fun apply(circuit: Circuit): Boolean {
                val v = op.getSignalStrength(circuit) ?: return false
                circuit[resultWire] = v.inv() and BIT_MASK
                return true
            }
        }


        abstract fun apply(circuit: Circuit): Boolean

        companion object {
            fun parseString(str: String): Instruction {
                val parsers = listOf<Pair<Regex, (List<String>) -> Instruction>>(
                    Pair(andRegex) { groups ->
                        And(
                            Operand.parseString(groups[1]),
                            Operand.parseString(groups[2]),
                            groups[3]
                        )
                    },
                    Pair(orRegex) { groups ->
                        Or(
                            Operand.parseString(groups[1]),
                            Operand.parseString(groups[2]),
                            groups[3]
                        )
                    },
                    Pair(lShiftRegex) { groups ->
                        LShift(
                            Operand.parseString(groups[1]),
                            Operand.parseString(groups[2]),
                            groups[3]
                        )
                    },
                    Pair(rShiftRegex) { groups ->
                        RShift(
                            Operand.parseString(groups[1]),
                            Operand.parseString(groups[2]),
                            groups[3]
                        )
                    },
                    Pair(notRegex) { groups ->
                        Not(
                            Operand.parseString(groups[1]),
                            groups[2]
                        )
                    },
                    Pair(assignRegex) { groups ->
                        Assign(
                            Operand.parseString(groups[1]),
                            groups[2]
                        )
                    }
                )

                val (regex, const) = parsers.first { it.first.matches(str) }
                val groups = (regex.matchEntire(str) ?: error("This should never happen")).groupValues
                return const(groups)
            }
        }
    }

    companion object {
        private const val BIT_MASK = 0xFFFF

        private val andRegex = "(.+) AND (.+) -> (\\w+)".toRegex()
        private val orRegex = "(.+) OR (.+) -> (\\w+)".toRegex()
        private val lShiftRegex = "(.+) LSHIFT (.+) -> (\\w+)".toRegex()
        private val rShiftRegex = "(.+) RSHIFT (.+) -> (\\w+)".toRegex()
        private val notRegex = "NOT (.+) -> (\\w+)".toRegex()
        private val assignRegex = "(.+) -> (\\w+)".toRegex()
    }
}
