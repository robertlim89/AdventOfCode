fun main(args: Array<String>) {
    Day6(args[0]).run()
}

class Day6(filename: String): BaseDay(filename) {
    private var start = Coord(0, 0)
    private val obstacles = mutableSetOf<Coord>()
    private var bounds = Coord(0, 0)

    fun process() {
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when(c) {
                    '^' -> start = Coord(x, y)
                    '#' -> obstacles.add(Coord(x, y))
                }
            }
        }
        bounds = Coord(lines[0].length, lines.size)
    }

    override fun part1(): String {
        process()
        return walk(obstacles).toString() // 5269
    }

    private fun walk(obstacles: Set<Coord>): Int {
        var delta = Coord(0, -1)
        val visited = mutableMapOf<Coord, MutableSet<Coord>>()
        var pos = start
        while(pos.inBounds(bounds)) {
            visited.computeIfAbsent(pos) {mutableSetOf() }
            if (!visited[pos]!!.add(delta)) {
                return -1
            }
            val new = pos.plus(delta)
            if (obstacles.contains(new)) {
                delta = delta.turnRight()
            } else {
                pos = new
            }
        }
        return visited.size
    }

    override fun part2(): String {
        //println(obstacles)
        return (0..bounds.x).sumOf { x ->
            (0..bounds.y).count { y ->
                val newBlocker = Coord(x, y)
                val newObstacles = obstacles.toMutableSet()
                newObstacles.add(newBlocker)
                if (newBlocker != start) {
                    if(walk(newObstacles) == -1) {
                        return@count true
                    }
                    return@count false
                }
                false
            }
        }.toString()
    }

    private fun Coord.turnRight(): Coord {
        return if (x == 0L && y == -1L) {
            Coord(1,0)
        } else if (x == 0L && y == 1L) {
            Coord(-1, 0)
        } else if (x == 1L && y == 0L) {
            Coord(0, 1)
        } else {
            Coord(0, -1)
        }
    }
}