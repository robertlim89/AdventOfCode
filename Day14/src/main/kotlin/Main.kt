import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>) {
    solve(args[0], args[1].equals("1"), args[2].equals("true"))
}

fun solve(filename: String, solvePart1: Boolean, showMap: Boolean) {
    val rockPaths = mutableListOf<CoordRange>()
    var minX = Int.MAX_VALUE
    var maxX = -1
    var maxY = -1
    File(filename).forEachLine { line ->
        val coords = line.split(" -> ").map { coord ->
            val ord = coord.split(",").map(String::toInt)
            minX = min(minX, ord[0])
            maxX = max(maxX, ord[0])
            maxY = max(maxY, ord[1])
            Coord(ord[0], ord[1])
        }
        rockPaths.addAll((0 until coords.size - 1).map {
            val first = coords[it]
            val second = coords[it + 1]
            if (first < second) return@map CoordRange(second, first)
            CoordRange(first, second)
        })
    }
    if (!solvePart1) rockPaths.add(CoordRange(Coord(Int.MAX_VALUE, maxY + 2), Coord(Int.MIN_VALUE, maxY + 2)))

    if (showMap) {
        printMap(rockPaths, mutableSetOf(), minX, maxX, maxY + 2)
    }

    val finalGrainPos = mutableSetOf<Coord>()
    val potentialDirections = listOf(0, -1, 1)
    (0..Int.MAX_VALUE).forEach { grain ->
        var grainPos = Coord(500, 0)
        var hasStopped = false
        while (!hasStopped) {
            hasStopped = true
            potentialDirections.firstOrNull { xDiff ->
                val potentialPos = Coord(grainPos.x + xDiff, grainPos.y + 1)
                if (!finalGrainPos.contains(potentialPos) && rockPaths.none { it.contains(potentialPos) }) {
                    grainPos = potentialPos
                    hasStopped = false
                    return@firstOrNull true
                }
                false
            }
            if (solvePart1 && grainPos.y >= maxY) {
                //finalGrainPos.add(grainPos) // Enable this if you want to see where the last grain ends up
                println("Only $grain grains fit before they fall")
                if (showMap) printMap(rockPaths, finalGrainPos, minX, maxX, maxY + 2)
                return
            }
        }
        finalGrainPos.add(grainPos)
        if (!solvePart1 && grainPos == Coord(500, 0)) {
            println("No more sand after ${grain + 1}")
            if (showMap) printMap(rockPaths, finalGrainPos, minX, maxX, maxY + 2)
            return
        }
    }

}

fun printMap(rocks: MutableList<CoordRange>, sand: MutableSet<Coord>, minX: Int, maxX: Int, maxY: Int) {
    val start = if(sand.isNotEmpty()) min(sand.map { it.x }.min(), minX) - 2 else minX
    val end = if(sand.isNotEmpty()) max(sand.map { it.x }.max(), maxX) + 2 else maxX
    (0..maxY).forEach { y ->
        (start..end).forEach { x ->
            val coord = Coord(x, y)
            if (rocks.any { it.contains(coord) }) print("█")
            else if (sand.contains(coord)) print(".")
            else print(" ")
        }
        println()
    }
}
