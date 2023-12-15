import java.io.File

fun main(args: Array<String>) {
    val rows = mutableListOf<Row>()
    File(args[0]).forEachLine { line ->
        val parsed = line.split(' ')
        val freqs = parsed[1].split(',').map { it.toInt() }
        val row = parsed[0]
        rows.add(Row(Array(5) {row}.joinToString("?").toCharArray(), )
    }

    val counts = rows.sumOf{
        val res = getPossibleMatches(it.line, it.line.getWildcards(), it.freq)
        val res2 = getPossibleMatches(it.line2, it.line2.getWildcards(), it.freq)
        val total = res * (res2 *res2 *res2*res2)
        println("Result of ${String(it.line)} = $res and $res2 = $total")
        total
    }
    println("Counts $counts")
}

fun CharArray.getWildcards(): List<Int> {
    return this.indices.filter{ this[it] == '?' }
}

data class Row(val line: CharArray, val freq: List<Int>)

fun getPossibleMatches(line: CharArray, wildcards: List<Int>, freq: List<Int>): Int {
    if(wildcards.isEmpty()) {
        return if (line.getFreq() == freq) 1 else 0
    }

    val wildcard = wildcards.first
    val newWildcards = wildcards.filter { it != wildcard }

    val without = line.clone()
    without[wildcard] = '.'
    val wo = getPossibleMatches(without, newWildcards, freq)

    val with = line.clone()
    with[wildcard] = '#'
    val w = getPossibleMatches(with, newWildcards, freq)
    return w + wo
}

fun CharArray.getFreq(): List<Int> {
    val list = mutableListOf<Int>()
    var freq = 0
    this.forEach {c ->
        if(c == '#') freq++
        else {
            if(freq != 0) list.add(freq)
            freq = 0
        }
    }
    if(freq != 0) list.add(freq)
    return list.toList()
}