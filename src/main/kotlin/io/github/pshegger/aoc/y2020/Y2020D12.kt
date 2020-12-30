package io.github.pshegger.aoc.y2020

import io.github.pshegger.aoc.common.BaseSolver
import io.github.pshegger.aoc.common.Coordinate
import kotlin.math.absoluteValue

class Y2020D12 : BaseSolver() {
    override val year = 2020
    override val day = 12

    override fun part1(): Int =
        parseInput().fold(Pair(Coordinate(0, 0), 90)) { acc, action ->
            val (c, facing) = acc
            when (action.name) {
                ActionsName.North -> Pair(c.copy(y = c.y + action.value), facing)
                ActionsName.South -> Pair(c.copy(y = c.y - action.value), facing)
                ActionsName.East -> Pair(c.copy(x = c.x + action.value), facing)
                ActionsName.West -> Pair(c.copy(x = c.x - action.value), facing)
                ActionsName.Left -> Pair(c, (360 + facing - action.value) % 360)
                ActionsName.Right -> Pair(c, (360 + facing + action.value) % 360)
                ActionsName.Forward -> when (facing) {
                    0 -> Pair(c.copy(y = c.y + action.value), facing)
                    90 -> Pair(c.copy(x = c.x + action.value), facing)
                    180 -> Pair(c.copy(y = c.y - action.value), facing)
                    270 -> Pair(c.copy(x = c.x - action.value), facing)
                    else -> error("Unknown facing $facing")
                }
            }
        }.let { (c, _) -> c.x.absoluteValue + c.y.absoluteValue }

    override fun part2(): Int =
        parseInput().fold(Pair(Coordinate(0, 0), Coordinate(10, 1))) { acc, action ->
            val (ship, wp) = acc
            when (action.name) {
                ActionsName.North -> Pair(ship, wp.copy(y = wp.y + action.value))
                ActionsName.South -> Pair(ship, wp.copy(y = wp.y - action.value))
                ActionsName.East -> Pair(ship, wp.copy(x = wp.x + action.value))
                ActionsName.West -> Pair(ship, wp.copy(x = wp.x - action.value))
                ActionsName.Left -> Pair(
                    ship,
                    (0 until (action.value / 90)).fold(wp) { (wpX, wpY), _ -> Coordinate(-wpY, wpX) }
                )
                ActionsName.Right -> Pair(
                    ship,
                    (0 until (action.value / 90)).fold(wp) { (wpX, wpY), _ -> Coordinate(wpY, -wpX) }
                )
                ActionsName.Forward -> Pair(
                    Coordinate(
                        ship.x + wp.x * action.value,
                        ship.y + wp.y * action.value
                    ),
                    wp
                )
            }
        }.let { (ship, _) -> ship.x.absoluteValue + ship.y.absoluteValue }

    private fun parseInput() = readInput {
        readLines().map { Action.parseString(it) }
    }

    private data class Action(val name: ActionsName, val value: Int) {

        companion object {

            fun parseString(str: String) = Action(
                ActionsName.values().first { it.value == str[0] },
                str.drop(1).toInt()
            )
        }
    }

    private enum class ActionsName(val value: Char) {
        North('N'),
        South('S'),
        East('E'),
        West('W'),
        Left('L'),
        Right('R'),
        Forward('F')
    }
}
