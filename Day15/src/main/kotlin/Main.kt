import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    solve(args[0], args[1].toInt(), args[2].equals("1"), args[3].equals("true"), args[4].toInt())
}

fun solve(filename: String, lineNumber: Int, solvePart1: Boolean, showMap: Boolean, searchLimit: Int) {
    val regex = """-?\d+""".toRegex()
    val sensors = mutableSetOf<Pair<Coord, Int>>()
    val beacons = mutableSetOf<Coord>()
    var minX = Int.MAX_VALUE
    var maxX = Int.MIN_VALUE
    var minY = Int.MAX_VALUE
    var maxY = Int.MIN_VALUE
    File(filename).forEachLine { line ->
        val coords = regex.findAll(line).map { it.value.toInt() }.toList()
        val sensor = Coord(coords[0], coords[1])
        val beacon = Coord(coords[2], coords[3])
        minX = listOf(minX, sensor.x, beacon.x).min()
        minY = listOf(minY, sensor.y, beacon.y).min()
        maxX = listOf(maxX, sensor.x, beacon.x).max()
        maxY = listOf(maxY, sensor.y, beacon.y).max()
        sensors.add(Pair(sensor, sensor.manhattanDistance(beacon)))
        beacons.add(beacon)
    }

    val special = sensors.find { p -> p.first == Coord(8, 7) }

    if (showMap) {
        (0..20).forEach { y ->
            (0..20).forEach { x ->
                val coord = Coord(x, y)
                if (sensors.any { it.first == coord }) print("S")
                else if (beacons.any { it == coord }) print("B")
                else if (sensors.any { it.second >= it.first.manhattanDistance(coord) }) print("#")
                //else if(special?.let {it.second >= it.first.manhattanDistance(coord)} == true) print("#")
                else print(".")
            }
            println()
        }
    }

    if (solvePart1) {
        val sansBeacons = mutableSetOf<Int>()
        sensors.forEach { (sensor, manhattan) ->
            val distToGoal = abs(sensor.y - lineNumber)
            val width = manhattan - distToGoal
            (sensor.x - width..sensor.x + width).forEach { x ->
                val pos = Coord(x, lineNumber)
                if (!beacons.contains(pos)) sansBeacons.add(x)
            }
        }

        println("line $lineNumber has ${sansBeacons.size}/${maxX - minX}  or  spots where there cannot be beacons")
    } else {
        val safeRanges = Array<MutableList<IntRange>>(searchLimit + 1) { mutableListOf() }
        sensors.forEach { (sensor, manhattan) ->
            (0..searchLimit).forEach { y ->
                val distToY = abs(sensor.y - y)
                val width = manhattan - distToY
                if (width > 0) {
                    safeRanges[y].add((sensor.x - width).coerceAtLeast(0)..(sensor.x + width).coerceAtMost(searchLimit))
                }
            }
        }

        beacons.forEach { beacon ->
            if(beacon.y in 0..searchLimit) safeRanges[beacon.y].add(beacon.x.coerceAtLeast(0)..beacon.x.coerceAtMost(searchLimit))
        }

        safeRanges.forEachIndexed { y, ranges ->
            val sortedRanges = ranges.sortedBy { it.first }
            var highest = sortedRanges.first().last
            sortedRanges.drop(1).forEach {range ->
                if(range.start > (highest+1)) {
                    println("Found at ${range.start} $y = ${(range.start-1L)*4000000+y}")
                }
                // End is bigger but the start was not
                else if (range.last > highest) {
                    highest = range.last
                }
            }
        }
    }

}