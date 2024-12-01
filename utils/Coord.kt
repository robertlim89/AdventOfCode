import kotlin.math.abs

data class Coord(val x: Long, val y: Long) : Comparable<Coord> {
    fun directNeighbours() : List<Coord> {
        val list = mutableListOf<Coord>()
        (-1..1).forEach { yy ->
            (-1..1).forEach { xx ->
                if ((xx == 0) != (yy == 0)) {
                    list.add(Coord(x + xx, y + yy))
                }
            }
        }
        return list
    }

    override fun compareTo(other: Coord): Int {
        return if (x != other.x) x.compareTo(other.x) else y.compareTo(other.y)
    }

    fun manhattanDistance(other: Coord): Long = abs(x - other.x) + abs(y - other.y)
}

data class LongCoord(val x: Long, val y: Long)

class CoordRange(override val endInclusive: Coord, override val start: Coord) : ClosedRange<Coord> {
    override operator fun contains(value: Coord): Boolean {
        return (start.x..endInclusive.x).contains(value.x) && (start.y..endInclusive.y).contains(value.y)
    }
}