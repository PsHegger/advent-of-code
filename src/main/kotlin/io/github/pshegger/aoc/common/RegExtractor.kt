package io.github.pshegger.aoc.common

class RegExtractor(searchString: String) {

    private val regex = searchString
        .replace("%d", "(\\d+)")
        .replace("%s",  "(\\w+)")
        .toRegex()

    fun <T> extract(text: String, block: (List<String>) -> T): T? =
        regex.matchEntire(text)
            ?.groupValues
            ?.drop(1)
            ?.let(block)
}
