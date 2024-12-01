import java.io.File

abstract class BaseDay(fileName: String) {
    protected val lines = File(fileName).readLines()

    fun run() {
        println("Part 1: ${part1()}")

        println("Part 2: ${part2()}")
    }

    open fun part1(): String {
        return "0"
    }

    open fun part2(): String {
        return "0"
    }
}