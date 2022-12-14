data class Coord(val x: Int, val y: Int) : Comparable<Coord> {
    fun directNeighbours() : List<Coord> {
        val list = mutableListOf<Coord>()
        (-1..1).forEach { yy ->
            (-1..1).forEach {xx ->
                if((xx == 0) != (yy == 0)) {
                    list.add(Coord(x + xx, y + yy))
                }
            }
        }
        return list
    }

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