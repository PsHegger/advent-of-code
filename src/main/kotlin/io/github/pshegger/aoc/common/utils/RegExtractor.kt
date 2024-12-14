package io.github.pshegger.aoc.common.utils

class RegExtractor(searchString: String) {

    private val regex = searchString
        .replace("+", "\\+")
        .replace("?", "\\?")
        .replace("*", "\\*")
        .replace("%d", "(-?\\d+)")
        .replace("%s",  "(\\w+)")
        .toRegex()

    fun <T> extract(text: String, block: (List<String>) -> T): T? =
        regex.matchEntire(text)
            ?.groupValues
            ?.drop(1)
            ?.let(block)
}
