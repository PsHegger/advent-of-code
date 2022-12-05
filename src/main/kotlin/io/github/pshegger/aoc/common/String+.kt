package io.github.pshegger.aoc.common

import java.security.MessageDigest

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    md.update(toByteArray())
    val digest = md.digest()
    return digest.toHex()
}

fun String.toExtractor() = RegExtractor(this)
