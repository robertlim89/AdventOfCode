import java.io.File

fun main(args: Array<String>) {
    val maps = mutableListOf<List<List<Char>>>()

    var map = mutableListOf<List<Char>>()
    File(args[0]).forEachLine { line ->
        if(line.isNotEmpty()) {
            map.add(line.toList())
        } else {
            maps.add(map.toList())
            map = mutableListOf()
        }

    }

    var total = 0L
    maps.forEach { lines ->
        val rowMirror = (1..lines.lastIndex).filter { row -> lines[row] == lines[row - 1] }
        val colMirror =
            (1..lines[0].lastIndex).find { col -> lines.map { line -> line[col] } == lines.map { line -> line[col - 1] } }
                ?: 0
        val result = rowMirror * 100 + colMirror
        println("${result}")
        total += result
    }

    println(total)
}