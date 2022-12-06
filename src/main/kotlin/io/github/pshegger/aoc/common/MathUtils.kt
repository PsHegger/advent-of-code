package io.github.pshegger.aoc.common

fun extendedGCD(a: Long, b: Long): Pair<Long, Long> {
    var oldR = a
    var r = b
    var oldS = 1L
    var s = 0L
    var oldT = 0L
    var t = 1L
    while (r != 0L) {
        val q = oldR / r
        var tmp = oldR
        oldR = r
        r = tmp - q * r
        tmp = oldS
        oldS = s
        s = tmp - q * s
        tmp = oldT
        oldT = t
        t = tmp - q * t
    }
    return Pair(oldS, oldT)
}

/**
 * Generate all the possible lists of integers, where the list contains exactly [splitCount] number of elements,
 * and the sum of the elements is [numberToSplit].
 *
 * Example:
 * [splitCount] = 2
 * [numberToSplit] = 5
 * result = [[0, 5], [1, 4], [2, 3], [3, 2], [4, 1], [5, 0]]
 */
fun generateSplits(splitCount: Int, numberToSplit: Int): List<List<Int>> =
    if (splitCount == 1) {
        listOf(listOf(numberToSplit))
    } else {
        (0..numberToSplit)
            .flatMap { a ->
                generateSplits(splitCount - 1, numberToSplit - a)
                    .map { listOf(a) + it }
            }
    }