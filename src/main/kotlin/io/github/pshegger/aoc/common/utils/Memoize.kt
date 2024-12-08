package io.github.pshegger.aoc.common.utils

class Memoize1<in T, out R>(val f: (T) -> R) : (T) -> R {
    private val values = mutableMapOf<T, R>()

    override fun invoke(x: T): R =
        values.getOrPut(x, { f(x) })
}

class Memoize2<in T1, in T2, out R>(val f: (T1, T2) -> R) : (T1, T2) -> R {
    private val values = mutableMapOf<Pair<T1, T2>, R>()

    override fun invoke(p1: T1, p2: T2): R =
        values.getOrPut(Pair(p1, p2), { f(p1, p2) })

}

class Memoize3<in T1, in T2, in T3, out R>(val f: (T1, T2, T3) -> R) : (T1, T2, T3) -> R {
    private val values = mutableMapOf<Triple<T1, T2, T3>, R>()

    override fun invoke(p1: T1, p2: T2, p3: T3): R =
        values.getOrPut(Triple(p1, p2, p3), { f(p1, p2, p3) })

}

fun <T, R> ((T) -> R).memoize(): (T) -> R = Memoize1(this)
fun <T1, T2, R> ((T1, T2) -> R).memoize(): (T1, T2) -> R = Memoize2(this)
fun <T1, T2, T3, R> ((T1, T2, T3) -> R).memoize(): (T1, T2, T3) -> R = Memoize3(this)
