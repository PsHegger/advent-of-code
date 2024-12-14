package io.github.pshegger.aoc

class ArgsParser(private val args: List<String>) {
    constructor(args: Array<String>) : this(args.toList())

    fun parse(): ArgsConfig {
        val benchmarkModeIndex = args.indexOfFirst { it == "benchmarks" }
        if (benchmarkModeIndex >= 0) {
            return ArgsConfig(Mode.Benchmark)
        }

        val yearFlagIndex = args.indexOfFirst { it == "--year" || it == "-y" }
        val year = if (yearFlagIndex >= 0 && yearFlagIndex + 1 < args.size) {
            args[yearFlagIndex + 1].toIntOrNull()
        } else {
            null
        }

        val dayFlagIndex = args.indexOfFirst { it == "--day" || it == "-d" }
        val day = if (dayFlagIndex >= 0 && dayFlagIndex + 1 < args.size) {
            args[dayFlagIndex + 1].toIntOrNull()
        } else {
            null
        }

        return ArgsConfig(Mode.Solver, year, day)
    }

    data class ArgsConfig(val mode: Mode, val year: Int? = null, val day: Int? = null)
}
