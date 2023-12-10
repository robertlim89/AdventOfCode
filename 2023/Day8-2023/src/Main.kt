import java.io.File

fun main(args: Array<String>) {
    var map = mutableMapOf<String, Pair<String, String>>()
    var moves = CharArray(0)
    var isSteps = true
    File(args[0]).forEachLine { line ->
        if(line.isNotEmpty()) {
            if(isSteps) {
                moves = line.toCharArray()
                isSteps = false
            } else {
                val p = line.split(" = ")
                val (left, right) = Regex("\\((\\w{3}), (\\w{3})\\)").find(p[1])!!.destructured
                map[p[0]] = left to right
            }
        }
    }

    val res = map.filter{ it.key.endsWith('A') }.map{
        getSteps(moves, map, it.key)
    }.reduce{ acc, i -> lcm(acc, i)}

    println(res)
}

fun lcm(a: Long, b: Long): Long {
    var ma = a
    var mb = b
    var remainder: Long

    while (mb != 0L) {
        remainder = ma % mb
        ma = mb
        mb = remainder
    }

    return a * b / ma
}

fun getSteps(moves: CharArray, map: Map<String, Pair<String, String>>, loc: String): Long {
    var current = loc
    var steps = 0

    while (!current.endsWith('Z')) {
        current = if (moves[steps % moves.size] == 'L') map[current]!!.first else map[current]!!.second
        steps++
    }
    println("Current $current step $steps")

    return steps.toLong()
}