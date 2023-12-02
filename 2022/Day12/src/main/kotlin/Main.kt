import java.io.File

fun main(args: Array<String>) {
    val map = mutableMapOf<Coord, Int>()
    val lines = File(args[0]).readLines()

    var start = Coord(0, 0)
    var end = Coord(0, 0)
    for((y, line) in lines.withIndex()) {
        for((x, char) in line.withIndex()) {
            map[Coord(x,y)] = char.toHeight()
            if(char == 'S') start = Coord(x, y)
            if(char == 'E') end = Coord(x, y)
        }
    }
    println("From $start: ${pathFind(start, end, map)}")
    val list = map.entries.filter { it.value == 1 }.map { it.key }
    println(list.minOfOrNull { pathFind(it, end, map) })
}

fun Char.toHeight(): Int {
    return "SabcdefghijklmnopqrstuvwxyzE".indexOfFirst { it == this }
}