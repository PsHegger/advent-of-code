package io.github.pshegger.aoc.common.extensions

import kotlin.math.*

fun Int.log10() = floor(log10(toFloat())).toInt()
fun Long.log10() = floor(log10(toFloat())).toLong()

val Int.isEven: Boolean get() = this % 2 == 0
val Int.isOdd: Boolean get() = !isEven

val Long.isEven: Boolean get() = this % 2L == 0L
val Long.isOdd: Boolean get() = !isEven

fun <T : Number> Int.pow(n: T) = toFloat().pow(n.toFloat()).roundToInt()
fun <T : Number> Long.pow(n: T) = toFloat().pow(n.toFloat()).roundToLong()