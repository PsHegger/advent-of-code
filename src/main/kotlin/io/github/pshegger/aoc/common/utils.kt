package io.github.pshegger.aoc.common

import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

fun downloadInputIfMissing(year: Int, day: Int) {
    val file = File("inputs/${year}_$day.txt")

    if (!file.exists()) {
        file.createNewFile()
        val session = System.getenv("AOC_SESSION")
        if (session == null) {
            file.delete()
            throw IllegalStateException("AOC_SESSION is not defined")
        }
        val url = URL("https://adventofcode.com/$year/day/$day/input")
        val connection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty("cookie", "session=$session")

        val inputStream = connection.inputStream
        val outStream = FileOutputStream(file)
        val buffer = ByteArray(8 * 1024)
        var bytesRead: Int
        do {
            bytesRead = inputStream.read(buffer)
            if (bytesRead >= 0) {
                outStream.write(buffer, 0, bytesRead)
            }
        } while (bytesRead != -1)

        inputStream.close()
        outStream.close()
    }
}

fun <T> Collection<Iterable<T>>.getCartesianProduct(): Set<List<T>> =
    if (isEmpty()) {
        emptySet()
    } else {
        drop(1).fold(first().map(::listOf)) { acc, iterable ->
            acc.flatMap { list -> iterable.map(list::plus) }
        }.toSet()
    }
