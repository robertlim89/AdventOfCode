fun pathFind(start: Coord, end: Coord, map: MutableMap<Coord, Int>): Int {
    val steps = hashMapOf<Coord, Int>()
    val parent = hashMapOf<Coord, Coord>()
    val queue = ArrayDeque<Coord>()
    steps[start] = 0
    queue.add(start)
    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        //println("Got $cur, h=${map[cur]}")
        if (node == end) {
            //println("  This is the end, steps = ${steps[cur]!!}")
            return steps[node] ?: Int.MAX_VALUE
        }
        for (neighbour in node.directNeighbours()) {
            //println("  Checking neighbour $neighbour")
            if (map[neighbour]?.let { it > (map[node]!! + 1) } != false) {
                //println("    OOB or too high")
                continue
            }
            val newSteps = steps[node]!! + 1
            if (newSteps < (steps[neighbour] ?: Int.MAX_VALUE)) {
                //println("    Updating steps from ${steps[neighbour] ?: Int.MAX_VALUE} to $newSteps")
                steps[neighbour] = newSteps
                parent[neighbour] = node
                queue.add(neighbour)
            }
        }
        //println()
    }
    return Int.MAX_VALUE
}