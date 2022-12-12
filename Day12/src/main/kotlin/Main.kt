import java.io.File

fun main(args: Array<String>) {
    val map = mutableListOf<List<Pair<Int, Type>>>()
    File(args[0]).forEachLine { line ->
        val heights = line.toCharArray().map { it.toHeight() }
        println(heights)
        map.add(heights)
    }
}

fun Char.toHeight() : Pair<Int, Type> {
    if(this == 'S') return Pair(1, Type.START)
    if(this == 'E') return Pair(26, Type.END)
    return "_abcdefghijklmnopqrstuvwxyz".indexOfFirst { it == this } to Type.PATH
}

enum class Type {
    START,
    END,
    PATH
}