package io.github.pshegger.aoc.y2024

import io.github.pshegger.aoc.common.model.BaseSolver
import io.github.pshegger.aoc.common.model.Coordinate
import io.github.pshegger.aoc.common.model.Direction
import io.github.pshegger.aoc.common.model.Grid

class Y2024D12 : BaseSolver() {
    override val year = 2024
    override val day = 12

    private val garden by lazy { parseInput() }
    private val regions by lazy { garden.findRegions() }

    override fun part1(): Int = regions.sumOf { it.size * it.calculatePerimeter() }
    override fun part2(): Int = regions.sumOf { it.size * it.getFences().size }

    private fun Grid<Char>.findRegions(): List<Set<Coordinate>> {
        val unprocessedCoords = keys.toMutableSet()
        val regions = mutableListOf<Set<Coordinate>>()

        while (unprocessedCoords.isNotEmpty()) {
            val startingCoordinate = unprocessedCoords.first()
            unprocessedCoords.remove(startingCoordinate)
            val plant = get(startingCoordinate)
            val region = mutableSetOf(startingCoordinate)
            val toVisit = startingCoordinate.cardinalNeighbours
                .filter { getOrNull(it) == plant && it in unprocessedCoords }
                .toMutableSet()

            while (toVisit.isNotEmpty()) {
                val coordinate = toVisit.first()
                toVisit.remove(coordinate)
                unprocessedCoords.remove(coordinate)
                region.add(coordinate)
                toVisit.addAll(
                    coordinate.cardinalNeighbours
                        .filter { getOrNull(it) == plant && it in unprocessedCoords }
                )
            }

            regions.add(region)
        }

        return regions
    }

    private fun Set<Coordinate>.calculatePerimeter(): Int = fold(0) { acc, coord ->
        acc + (4 - coord.cardinalNeighbours.count { it in this })
    }

    private fun Set<Coordinate>.getFences(): List<List<Coordinate>> {
        val northFences = mutableListOf<MutableList<Coordinate>>()
        val westFences = mutableListOf<MutableList<Coordinate>>()
        val eastFences = mutableListOf<MutableList<Coordinate>>()
        val southFences = mutableListOf<MutableList<Coordinate>>()

        sortedBy { it.y * garden.width + it.x }
            .forEach { coordinate ->
                val westernNeighbour = coordinate + Direction.West.asCoordinate()
                val northernNeighbour = coordinate + Direction.North.asCoordinate()
                val easternNeighbour = coordinate + Direction.East.asCoordinate()
                val southernNeighbour = coordinate + Direction.South.asCoordinate()

                northFences.extendOrAdd(this, coordinate, northernNeighbour, westernNeighbour)
                westFences.extendOrAdd(this, coordinate, westernNeighbour, northernNeighbour)
                eastFences.extendOrAdd(this, coordinate, easternNeighbour, northernNeighbour)
                southFences.extendOrAdd(this, coordinate, southernNeighbour, westernNeighbour)
            }
        return northFences + westFences + eastFences + southFences
    }

    private fun MutableList<MutableList<Coordinate>>.extendOrAdd(region: Set<Coordinate>, coordinate: Coordinate, n1: Coordinate, n2: Coordinate) {
        if (n1 !in region) {
            if (n2 !in region) {
                add(mutableListOf(coordinate))
            } else {
                val existingFence = firstOrNull { it.last() == n2 }
                if (existingFence != null) {
                    existingFence.add(coordinate)
                } else {
                    add(mutableListOf(coordinate))
                }
            }
        }
    }

    private fun parseInput() = readInput {
        Grid.fromLines(readLines(), { '-' }) { it }
    }
}