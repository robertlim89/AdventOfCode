import java.io.File
import kotlin.system.measureNanoTime

fun main(args: Array<String>) {
    val emptyRows = mutableListOf<Int>()
    var galaxies = mutableListOf<Coord>()
    val lines = File(args[0]).readLines()
    lines.forEachIndexed{ row, line ->
        var hasGalaxy = false
        line.forEachIndexed { col, c ->
            if(c == '#') {
                galaxies.add(Coord(col, row))
                hasGalaxy = true
            }
        }
        if (!hasGalaxy) emptyRows.add(row)
    }
    val xs = galaxies.map{ it.x }.toList()
    val emptyCols = lines[0].indices.toMutableList()
    emptyCols.removeAll(xs)
    galaxies = galaxies.map { galaxy ->
        val countX = emptyCols.count{ it < galaxy.x }
        val countY = emptyRows.count{ it < galaxy.y }
        Coord(galaxy.x + countX*999999, galaxy.y+countY*999999)
    }.toMutableList()

    var total = 0L
    for(i in 0..galaxies.lastIndex) {
        for(j in i+1..galaxies.lastIndex) {
            val dist = galaxies[i].manhattanDistance(galaxies[j])
            println("Dist between galaxy $i and $j is $dist")
            total += dist
        }
    }
    println("total $total")
}