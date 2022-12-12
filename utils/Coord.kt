data class Coord(val x: Int, val y: Int) {
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
}