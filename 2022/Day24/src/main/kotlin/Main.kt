import java.io.File
import java.lang.IllegalArgumentException

fun main(args: Array<String>) {
    solve(args[0])
}

fun solve(filename: String) {
    val blizzards = mutableMapOf<Int, MutableMap<Int, Direction>>()
    var start: Coord? = null
    var end: Coord? = null
    val lastLine = File(filename).readLines().size - 1
    for((y, line) in File(filename).readLines().withIndex()) {
        val row = mutableMapOf<Int, Direction>()
        for((x, c) in line.toCharArray().withIndex()) {
            if(c == '<' || c == '>' || c == 'v' || c == '^') {
                row[x] = when(c) {
                    '>' -> Direction.RIGHT
                    '<' -> Direction.LEFT
                    '^' -> Direction.UP
                    'v' -> Direction.DOWN
                    else -> throw IllegalArgumentException("Huh?")
                }
            }
            if(c == '.') {
                if(y == 0) {
                    start = Coord(x,y)
                } else if (y==lastLine) {
                    end = Coord(x,y)
                }
            }
        }
        if(row.isNotEmpty()) blizzards[y] = row
    }

}

enum class Type {
    EMPTY,
    WALL,
    BLIZZARD
}

enum class Direction(val value: Int) {
    RIGHT(0),
    DOWN(1),
    LEFT(2),
    UP(3)
}