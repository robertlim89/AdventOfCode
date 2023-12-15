import java.io.File
import java.util.*
import kotlin.math.max

fun main(args: Array<String>) {
    val rocks = initialiseRocks()
    val windDirections = File(args[0]).readLines()[0].toCharArray().map { it == '<' }
    val floor = mutableMapOf<Int, SortedSet<Int>>()
    floor[0] = (0..6).toSortedSet()
    var floorHeight = 0
    //var rockCount = 0
    var windCycle = 0
    var cycleSyncNum: Int? = null
    val seenCycles = mutableSetOf<Int>()
    var cycleStartRock: Coord? = null
    var cycleStartRockCount = -1
    val partialCycleHeights = mutableListOf<Int>()

    repeat(100000) {rockCount ->
        var currentRock = rocks[rockCount % 5].toMutableList().shiftFloor(floorHeight)
        while (true) {
            val windDir = windDirections[windCycle]
            windCycle = (windCycle + 1) % windDirections.size
            currentRock = currentRock.shiftOneHorizontal(windDir, floor, 7)
            if (currentRock.canDrop(floor)) {
                currentRock = currentRock.map { Coord(it.x, it.y - 1) }.toMutableList()
            } else {
                break
            }
        }

        currentRock.forEach { coord ->
            floor.computeIfAbsent(coord.y) { sortedSetOf() }.add(coord.x)
        }
        floorHeight = max(currentRock.last().y, floorHeight)

        if (rockCount % 5 == 4) {
            if (cycleSyncNum == windCycle) {
                val cycleLength = rockCount - cycleStartRockCount
                val cycleHeight = currentRock.first().y - cycleStartRock!!.y
                val remainingRocks = args[1].toLong() - cycleStartRockCount
                val remainingCycles = remainingRocks / cycleLength
                val extraRocks = (remainingRocks % cycleLength).toInt()
                println(
                    "rock #${rockCount}: cycleHeight = $cycleHeight, cycleLength= $cycleLength, rocksToJump = $remainingRocks," +
                            " cyclesToJump = $remainingCycles, partialCycleHeights[$extraRocks] = ${partialCycleHeights[extraRocks]}"
                )
                println("Height = ${remainingCycles * cycleHeight + partialCycleHeights[extraRocks]}")
                return
            } else if (cycleSyncNum == null) {
                if (!seenCycles.add(windCycle)) {
                    cycleSyncNum = windCycle
                    cycleStartRock = currentRock.first()
                    cycleStartRockCount = rockCount
                    println("Catching cycle $windCycle at rock #${rockCount}")
                }
            }
        }
        if (cycleStartRock != null) {
            partialCycleHeights.add(floorHeight - 1)
        }
    }
    println(floorHeight)
}

fun initialiseRocks(): List<List<Coord>> = listOf(
    listOf(
        Coord(2, 4),
        Coord(3, 4),
        Coord(4, 4),
        Coord(5, 4)
    ),
    listOf(
        Coord(3, 4),
        Coord(2, 5),
        Coord(3, 5),
        Coord(4, 5),
        Coord(3, 6)
    ),
    listOf(
        Coord(2, 4),
        Coord(3, 4),
        Coord(4, 4),
        Coord(4, 5),
        Coord(4, 6)
    ),
    listOf(
        Coord(2, 4),
        Coord(2, 5),
        Coord(2, 6),
        Coord(2, 7)
    ),
    listOf(
        Coord(2, 4),
        Coord(3, 4),
        Coord(2, 5),
        Coord(3, 5)
    )
)

fun MutableList<Coord>.shiftOneHorizontal(shiftLeft: Boolean, floor: MutableMap<Int, SortedSet<Int>>, width: Int):
        MutableList<Coord> {
    val canMove =
        if (shiftLeft) this.none { coord ->
            (floor[coord.y]?.lastOrNull { it < coord.x } ?: -1) == (coord.x - 1)
        }
        else this.none { coord ->
            (floor[coord.y]?.firstOrNull { it > coord.x } ?: width) == (coord.x + 1)
        }
    val offset = if (canMove) 1 * if (shiftLeft) -1 else 1 else 0
    return this.map { Coord(it.x + offset, it.y) }.toMutableList()
}

fun MutableList<Coord>.canDrop(floor: MutableMap<Int, SortedSet<Int>>): Boolean {
    return this.none { coord ->
        floor[coord.y - 1]?.contains(coord.x) == true
    }
}

fun MutableList<Coord>.shiftFloor(units: Int): MutableList<Coord> =
    this.map { Coord(it.x, it.y + units) }.toMutableList()
