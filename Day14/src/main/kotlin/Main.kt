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
    if(!solvePart1) rockPaths.add(CoordRange(Coord(Int.MAX_VALUE, maxY+2), Coord(Int.MIN_VALUE, maxY+2)))

    if(showMap) {
        (0..maxY + 2).forEach { y ->
            (minX..maxX).forEach { x ->
                if (rockPaths.any { it.contains(Coord(x, y)) }) print("#") else print(" ")
            }
            println()
        }
    }

    val finalGrainPos = mutableSetOf<Coord>()
    val potentialDirections = listOf(0, -1, 1)
    (0..Int.MAX_VALUE).forEach { grain ->
        var grainPos = Coord(500, 0)
        var hasStopped = false
        while(!hasStopped) {
            hasStopped = true
            // Try down
            potentialDirections.firstOrNull { xDiff ->
                val potentialPos = Coord(grainPos.x + xDiff, grainPos.y+1)
                if(!finalGrainPos.contains(potentialPos) && rockPaths.none { it.contains(potentialPos)}) {
                    grainPos = potentialPos
                    hasStopped = false
                    return@firstOrNull true
                }
                false
            }
            if(solvePart1 && grainPos.y >= maxY) {
                println("Only $grain grains fit before they fall")
                return
            }
        }
        if(!solvePart1 && grainPos == Coord(500, 0)) {
            println("No more sand after ${grain+1}")
            return
        }
        finalGrainPos.add(grainPos)
    }
}

data class Coord(var x: Int, var y: Int) : Comparable<Coord> {
    override fun compareTo(other: Coord): Int {
        if (x != other.x) return x - other.x
        return y - other.y
    }
}

class CoordRange(override val endInclusive: Coord, override val start: Coord) : ClosedRange<Coord> {
    override operator fun contains(value: Coord): Boolean {
        return (start.x..endInclusive.x).contains(value.x) && (start.y..endInclusive.y).contains(value.y)
    }
}