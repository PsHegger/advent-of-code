package io.github.pshegger.aoc.common

private val HEX_ARRAY = "0123456789ABCDEF".toCharArray()

fun ByteArray.toHex(): String {
    val hexChars = CharArray(size * 2)
    for (i in indices) {
        val v = this[i].toInt() and 0xFF
        hexChars[i * 2] = HEX_ARRAY[v ushr 4]
        hexChars[i * 2 + 1] = HEX_ARRAY[v and 0x0F]
    }
    return String(hexChars)
}
