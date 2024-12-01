import java.util.*

fun main(args: Array<String>) {
    Day21(args[0]).run()
}

class Day21(fileName: String): BaseDay(fileName) {

    private var map = mutableMapOf<Coord, Char>()
    override fun part1(): String {
        lines.forEachIndexed { row, line ->
            line.forEachIndexed { col, c ->
                if(c != '.') {
                    map[Coord(col.toLong(), row.toLong())] = c
                }
            }
        }
        val maxX = lines[0].lastIndex
        val maxY = lines.lastIndex

        val start = map.entries.first { (_, v) -> v == 'S' }.key
        val queue = ArrayDeque<Pair<Coord, Int>>()
        queue.add(start to 0)
        var reached = mutableSetOf<Coord>()
        while(queue.isNotEmpty()) {
            val (loc, steps)  = queue.removeFirst()
            if (steps == 64) {
                reached.add(loc)
            } else {
                for(neighbour in loc.directNeighbours()) {
                    if (neighbour.x in 0..maxX && neighbour.y in 0..maxY && map[neighbour] != '#' && neighbour !in reached) {
                        queue.add(neighbour to steps + 1)
                    }
                }
            }
        }
        //printMap(reached, maxX, maxY)
        return reached.size.toString()
    }

    fun printMap(potentials: Set<Coord>, maxX: Int, maxY: Int) {
        (0L..maxY).forEach {y ->
            (0L..maxX).forEach { x ->
                val coord = Coord(x,y)
                if(map.contains(coord)) {
                    print(map[coord])
                } else if(potentials.contains(coord)) {
                    print('O')
                } else {
                    print('.')
                }
            }
            println()
        }
    }
}