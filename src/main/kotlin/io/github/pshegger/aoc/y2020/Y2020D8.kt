package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.extensions.updated

class Y2020D8 : BaseSolver() {
    override val year = 2020
    override val day = 8

    override fun part1(): Int {
        val runner = ProgramRunner(parseInput())
        val watchdogs = (0 until runner.instructionCount).map { 0 }.toMutableList()
        while (!runner.isFinished && watchdogs[runner.pcr] == 0) {
            watchdogs[runner.pcr]++
            runner.step()
        }
        return runner.acc
    }

    override fun part2(): Int {
        val instructions = parseInput()
        instructions.indices.forEach { i ->
            val instruction = instructions[i].name
            var runner: ProgramRunner? = null
            if (instruction == "jmp") {
                runner = ProgramRunner(instructions.updated(i, instructions[i].copy(name = "nop")))
            } else if (instruction == "nop") {
                runner = ProgramRunner(instructions.updated(i, instructions[i].copy(name = "jmp")))
            }
            runner?.let {
                if (it.runProgram()) {
                    return it.acc
                }
            }
        }
        return -1
    }

    private fun parseInput() = readInput {
        val program = readLines().map { line ->
            val parts = line.split(" ", limit = 2)
            val args = parts[1].split(" ").map { it.toInt() }
            Instruction(parts[0], args)
        }
        program
    }

    private data class Instruction(val name: String, val args: List<Int>)

    private data class ProgramRunner(val program: List<Instruction>, var acc: Int = 0, var pcr: Int = 0) {

        val instructionCount: Int get() = program.size
        val isFinished: Boolean get() = pcr >= instructionCount

        fun step() {
            evalInstruction()
            pcr++
        }

        fun runProgram(): Boolean {
            var watchdog = 0
            while (!isFinished) {
                if (watchdog > instructionCount * 10) {
                    return false
                }
                step()
                watchdog++
            }
            return true
        }

        private fun evalInstruction() {
            val instruction = program[pcr]
            when (instruction.name) {
                "acc" -> acc += instruction.args[0]
                "jmp" -> pcr += instruction.args[0] - 1
            }
        }
    }
}
