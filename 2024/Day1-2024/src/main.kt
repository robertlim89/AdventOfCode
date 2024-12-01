import kotlin.math.abs

fun main(args: Array<String>) {
    Day1(args[0]).run()
}

class Day1(fileName: String): BaseDay(fileName) {
    override fun part1(): String {
        val first = mutableListOf<Int>()
        val second = mutableListOf<Int>()
        lines.forEach{ line ->
            val items = line.split(' ').filter { it.isNotEmpty() }
            first.add(items[0].toInt())
            second.add(items[1].toInt())
        }
        first.sort()
        second.sort()
        return first.indices.sumOf { abs(first[it]-second[it]) }.toString()
    }

    override fun part2(): String {
        val keys = mutableListOf<Int>()
        val freq = mutableMapOf<Int, Int>()
        lines.forEach { line ->
            val items = line.split(' ').filter { it.isNotEmpty() }
            keys.add(items[0].toInt())
            freq.put(items[1].toInt(), freq.getOrDefault(items[1].toInt(), 0)+1)
        }
        return keys.sumOf { 1L*it*freq.getOrDefault(it, 0) }.toString()
    }
}