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

    val pos = map.keys.filter { it[2] == 'A' }.toTypedArray()
    val cycles = Array<MutableSet<String>>(pos.size) { mutableSetOf() }
    val cycleSync = Array<Int?>(pos.size) { null }
    val cycleStartPos = Array(pos.size) { "" }
    val cycleLength = Array<MutableList<Int>>(pos.size) { mutableListOf() }
    var ind = 0

    repeat(100000) {
        pos.indices.forEach{ id ->
            pos[id] = if (moves[ind % moves.size] == 'L') map[pos[id]]!!.first else map[pos[id]]!!.second


            if(cycleLength[id].size < 6 && cycleSync[id] != null && pos[id][2] == 'Z') {
                println("${pos[id]} $ind ${cycleSync[id]}")
                val new = ind - (if(cycleLength[id].isEmpty()) cycleSync[id] else cycleLength[id].last)!!
                cycleLength[id].add(new)
            }

            if (cycleSync[id] == null && !cycles[id].add(pos[id])) {
                cycleSync[id] = ind
                cycleStartPos[id] = pos[id]
            }
        }
        ind++
    }
    println("Cycle Length: ${cycleLength.toList()}")
    println("Cycles: ${cycleSync.toList()}")
}