import kotlin.math.abs

data class Coord(val x: Long, val y: Long) : Comparable<Coord> {
    constructor(x: Int, y: Int): this(x.toLong(), y.toLong())

    fun directNeighbours() : List<Coord> {
        val list = mutableListOf<Coord>()
        (-1..1).forEach { yy ->
            (-1..1).forEach { xx ->
                if (!(xx == 0 && yy == 0)) {
                    list.add(Coord(x + xx, y + yy))
                }
            }
        }
        return list
    }

    override fun compareTo(other: Coord): Int {
        return if (x != other.x) x.compareTo(other.x) else y.compareTo(other.y)
    }

    fun inBounds(bounds: Coord) : Boolean {
        return x >= 0 && x < bounds.x && y >= 0 && y < bounds.y
    }

    fun plus(other: Coord) = Coord(x + other.x, y + other.y)
    fun minus(other: Coord) = Coord(x - other.x, y - other.y)

    fun manhattanDistance(other: Coord): Long = abs(x - other.x) + abs(y - other.y)
}

class CoordRange(override val endInclusive: Coord, override val start: Coord) : ClosedRange<Coord> {
    override operator fun contains(value: Coord): Boolean {
        return (start.x..endInclusive.x).contains(value.x) && (start.y..endInclusive.y).contains(value.y)
    }
}