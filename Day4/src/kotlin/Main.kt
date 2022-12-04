import java.io.File

fun main(args: Array<String>) {
    var count = 0
    File(args[0]).forEachLine {line ->
        val ranges = line.split(',')
        val elves = ranges.map {
            val rangeBounds = it.split('-')
            IntRange(rangeBounds[0].toInt(), rangeBounds[1].toInt())
        }
        val isSubsumed = (elves[0].contains(elves[1].first) || elves[0].contains(elves[1].last)) || (elves[1]
            .contains(elves[0].first) || elves[1].contains(elves[0].last))
        count += if(isSubsumed) 1 else 0
        println("Is range ${elves[0]} and ${elves[1]} a subset: $isSubsumed")
    }
    println("Subsumed $count")
}