import java.io.File
import java.util.*
import kotlin.math.max

fun main(args: Array<String>) {
    val map = mutableMapOf<Coord, Char>()
    val lines = File(args[0]).readLines()

    var animal = Coord(0, 0)
    for ((y, line) in lines.withIndex()) {
        for ((x, char) in line.withIndex()) {
            map[Coord(x, y)] = char
            if (char == 'S') animal = Coord(x, y)
        }
    }
    val size = lines[0].length to lines.size
    val steps = pathFind(animal, map, size)
    println("Steps $steps")
}

fun pathFind(start: Coord, map: MutableMap<Coord, Char>, size: Pair<Int, Int>): Pair<Int, Int> {
    val queue = Stack<Pair<Coord, Int>>()
    queue.add(start to 0)
    var maxLoop = 0
    val vertices = mutableListOf(start)
    //val visited = mutableSetOf<Coord>()
    while (queue.isNotEmpty()) {
        val (node, steps) = queue.pop()
        maxLoop = max(maxLoop, steps)
        for (neighbour in map[node]!!.getNeighbours(node)) {
            if (!map.contains(neighbour) || map[neighbour] == '.' || queue.any{ it.first == neighbour }) {
                continue
            }
            if (neighbour !in vertices) {
                vertices.add(neighbour)
                queue.push(neighbour to steps + 1)
            }

        }
    }
    return (maxLoop / 2) + 1 to vertices.getArea(map, size.first, size.second)
}

fun List<Coord>.getArea(map: MutableMap<Coord, Char>, width: Int, height: Int): Int {
    var area = 0

    for(y in 0 until height) {
        var windings = 0
        for(x in 0 until width) {
            val point = Coord(x, y)
            val value = map[point]!!
            if (point in this && value in "|S7J") windings++
            if (point !in this && windings % 2 == 1) {
                println("Point $point = ${map[point]} $windings")
                area++
            }
        }
    }
    return area
}

fun Char.getNeighbours(loc: Coord): List<Coord> {
    return when (this) {
        'F' -> listOf(Coord(loc.x, loc.y + 1), Coord(loc.x + 1, loc.y))
        '7' -> listOf(Coord(loc.x, loc.y + 1), Coord(loc.x - 1, loc.y))
        'J' -> listOf(Coord(loc.x, loc.y - 1), Coord(loc.x - 1, loc.y))
        'L' -> listOf(Coord(loc.x, loc.y - 1), Coord(loc.x + 1, loc.y))
        '|' -> listOf(Coord(loc.x, loc.y - 1), Coord(loc.x, loc.y + 1))
        '-' -> listOf(Coord(loc.x - 1, loc.y), Coord(loc.x + 1, loc.y))
        'S' ->  listOf(Coord(loc.x - 1, loc.y), Coord(loc.x + 1, loc.y), Coord(loc.x, loc.y - 1), Coord(loc.x, loc.y + 1))
        else -> listOf()
    }
}