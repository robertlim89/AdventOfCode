import kotlin.math.abs

fun main(args: Array<String>) {
    Day2(args[0]).run()
}

class Day2(fileName: String): BaseDay(fileName) {
    override fun part1(): String {
        var safe = 0
        lines.forEach lineBreak@ { line ->
            val items = line.split(' ').filter { it.isNotEmpty() }.map { it.toInt() }
            val gaps = IntArray(items.size-1) {0}
            gaps.indices.forEach {
                gaps[it] = items[it+1] - items[it]
            }
            if (!gaps.isSafe()) return@lineBreak
            println("$line is safe")
            safe++
        }
        return safe.toString()
    }

    override fun part2(): String {
        var safe = 0
        lines.forEach lineBreak@{ line ->
            val items = line.split(' ').filter { it.isNotEmpty() }.map { it.toInt() }
            val gaps = IntArray(items.size-1) {0}
            gaps.indices.forEach {
                gaps[it] = items[it+1] - items[it]
            }
            if (gaps.isSafe() || checkSafe(items)) {
                safe++
            } else {
                println("$line is unsafe")
            }
        }
        return safe.toString()
    }

    private fun IntArray.isSafe(): Boolean {
        return (this.all { it > 0 } || this.all { it < 0})
                && this.all{ abs(it) in 1..3 }
    }

    private fun checkSafe(items: List<Int>): Boolean {
        val gaps = IntArray(items.size-2) {0}
        items.indices.forEach{ remove ->
            val newItems = items.toMutableList()
            newItems.removeAt(remove)
            println(newItems)

            gaps.indices.forEach {
                gaps[it] = newItems[it+1] - newItems[it]
            }
            if (gaps.isSafe()) {
                //println("$items can be made safe")
                return true
            }
        }
        return false
    }
}
