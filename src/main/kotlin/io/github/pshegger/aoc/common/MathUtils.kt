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
