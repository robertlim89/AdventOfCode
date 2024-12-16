fun pathFind(start: Coord, end: Coord, map: MutableMap<Coord, Int>, includeDiagonal: Boolean = true, allowDownstep: Boolean = true, maxStep: Int = 1): Int {
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
        for (neighbour in node.neighbours(includeDiagonal)) {
            //println("  Checking neighbour $neighbour")
            if (map[neighbour]?.let { (it > (map[node]!! + maxStep)) || (it <= map[node]!! && !allowDownstep) } != false) {
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

fun pathFindParents(start: Coord, end: Coord, map: MutableMap<Coord, Int>, includeDiagonal: Boolean = true, allowDownstep: Boolean = true, maxStep: Int = 1): Int {
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
        for (neighbour in node.neighbours(includeDiagonal)) {
            //println("  Checking neighbour $neighbour")
            if (map[neighbour]?.let { (it > (map[node]!! + maxStep)) || (it <= map[node]!! && !allowDownstep) } != false) {
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

