package io.github.pshegger.aoc.y2024

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.model.Coordinate
import io.github.pshegger.aoc.common.model.Direction
import io.github.pshegger.aoc.common.model.Grid

class Y2024D6 : BaseSolver() {
    override val year = 2024
    override val day = 6

    private val input by lazy { parseInput() }
    private val grid by lazy { input.first }
    private val guardStart by lazy { input.second }
    private val guardDirection by lazy { input.third }

    override fun part1() = simulateRoute()
        .first
        .map { it.first }
        .toSet()
        .size

    override fun part2(): Int {
        val (route, _) = simulateRoute()
        val obstacles = mutableSetOf<Coordinate>()
        val baseVisited = mutableSetOf<Pair<Coordinate, Direction>>()
        val visitedCoords = mutableSetOf<Coordinate>()

        route.forEach { step ->
            val (c, dir) = step
            baseVisited.add(step)
            visitedCoords.add(c)

            val dirCoords = dir.asCoordinate()
            val obstacle = c + dirCoords
            if (visitedCoords.contains(obstacle)) {
                return@forEach
            }

            val (_, isLooping) = simulateRoute(
                startCoord = c,
                startDir = dir.turnRight(),
                alreadyVisited = baseVisited,
                obstacle = obstacle
            )

            if (isLooping) {
                obstacles.add(obstacle)
            }
        }

        return obstacles.size
    }

    private fun simulateRoute(
        startCoord: Coordinate = guardStart,
        startDir: Direction = guardDirection,
        alreadyVisited: Set<Pair<Coordinate, Direction>> = emptySet(),
        obstacle: Coordinate? = null,
    ): Pair<List<Pair<Coordinate, Direction>>, Boolean> {
        var currentCoords = startCoord
        var currentDir = startDir
        val route = mutableListOf<Pair<Coordinate, Direction>>()
        val visited = HashSet(alreadyVisited)
        var isLooping = false

        while (grid.isInside(currentCoords)) {
            visited.add(Pair(currentCoords, currentDir))
            route.add(Pair(currentCoords, currentDir))
            val newCoords = currentCoords + currentDir.asCoordinate()
            if (canMoveTo(newCoords, obstacle)) {
                currentCoords = newCoords
            } else {
                currentDir = currentDir.turnRight()
            }
            if (visited.contains(Pair(currentCoords, currentDir))) {
                isLooping = true
                break
            }
        }

        return Pair(route, isLooping)
    }

    private fun canMoveTo(c: Coordinate, extraObstacle: Coordinate? = null): Boolean {
        if (!grid.isInside(c)) {
            return true
        }
        if (grid[c] || c == extraObstacle) {
            return false
        }

        return true
    }

    private fun parseInput() = readInput {
        val lines = readLines()
        val grid = Grid(lines[0].length, lines.size) { false }
        var guardStart = Coordinate(0, 0)
        var guardDirection = Direction.North

        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when (c) {
                    '^' -> {
                        guardStart = Coordinate(x, y)
                        guardDirection = Direction.North
                    }
                    '>' -> {
                        guardStart = Coordinate(x, y)
                        guardDirection = Direction.East
                    }
                    '<' -> {
                        guardStart = Coordinate(x, y)
                        guardDirection = Direction.West
                    }
                    'v', 'V' -> {
                        guardStart = Coordinate(x, y)
                        guardDirection = Direction.South
                    }
                    '#' -> {
                        grid[x, y] = true
                    }
                }
            }
        }

        Triple(grid, guardStart, guardDirection)
    }
}
