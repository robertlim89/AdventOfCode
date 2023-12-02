
import java.io.File
import java.util.PriorityQueue
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main(args: Array<String>) {
    solve2(args[0], args[1].toInt())
}

fun solve(filename: String, time: Int) {
    val regex = """\d+""".toRegex()
    val regex2 = """([A-Z]{2})""".toRegex()
    val valves = mutableMapOf<String, Valve>()
    File(filename).forEachLine { line ->
        val flow = regex.find(line)?.groupValues?.get(0)?.toInt()
        val pipes = regex2.findAll(line).map {it.value}.toList()
        flow?.let { valves[pipes[0]] = Valve(pipes[0], it, pipes.drop(1)) }
    }

    //val parentMap = prims(valves.toMap(), startPipe)
    //println(parentMap)

    val totalPressure = 0
    val openValves = mutableListOf<String>()
    val moveQueue = ArrayDeque<String>()

    val distanceMap = valves.map { it.key to it.value.getDistancesToOthers(valves)}
    val valveOptions = valves.filter { it.value.flowRate > 0 }.values.toList()
    val start = valves["AA"]!!

    val paths = getPathPermutations(start, valveOptions, distanceMap, time)
    println(paths.maxOf { it.second })
}

fun solve2(filename: String, time: Int) {
    val regex = """\d+""".toRegex()
    val regex2 = """([A-Z]{2})""".toRegex()
    val valves = mutableMapOf<String, Valve>()
    File(filename).forEachLine { line ->
        val flow = regex.find(line)?.groupValues?.get(0)?.toInt()
        val pipes = regex2.findAll(line).map {it.value}.toList()
        flow?.let { valves[pipes[0]] = Valve(pipes[0], it, pipes.drop(1)) }
    }

    //val parentMap = prims(valves.toMap(), startPipe)
    //println(parentMap)

    val totalPressure = 0
    val openValves = mutableListOf<String>()
    val moveQueue = ArrayDeque<String>()

    val distanceMap = valves.map { it.key to it.value.getDistancesToOthers(valves)}
    val valveOptions = valves.filter { it.value.flowRate > 0 }.values.toList()
    val start = valves["AA"]!!

    val paths = getPathPermutations(start, valveOptions, distanceMap, time)

    val bestScore = AtomicInteger()

    runBlocking {
        withContext(Dispatchers.Default) {
            paths.forEachIndexed { index, pair ->
                //println("Best score: ${bestScore.get()} ($index / ${paths.size})")

                getPathPermutations(start, valveOptions, distanceMap, time, pair.first).forEach {
                    if(pair.second + it.second > bestScore.get()) {
                        bestScore.set(pair.second + it.second)
                        //println("New best score: (${pair.second + it.second}) ${pair.first} ${it.first} ")
                    }
                }
            }
        }
    }

    println(bestScore.get())
}

fun getPathPermutations(
    startingSpace: Valve,
    spaces: List<Valve>,
    distanceMap: List<Pair<String, List<Pair<String, Int>>>>,
    time: Int,
    visitedSpaces: List<String> = listOf()
): List<Pair<List<String>, Int>> {
    val permutations = mutableListOf<Pair<List<String>, Int>>()

    fun getAllPaths(
        pathHead: Valve,
        currentPath: Pair<List<String>, Int>,
        minutesRemaining: Int
    ): Set<Pair<List<String>, Int>> {

        val remainingSpaces = spaces.filter {
            !visitedSpaces.contains(it.name) && !currentPath.first.contains(it.name) && minutesRemaining >= (distanceMap.distanceBetween(
                pathHead.name,
                it.name
            ) + 1)
        }

        return if (remainingSpaces.isNotEmpty()) {
            remainingSpaces.flatMap {
                getAllPaths(
                    it,
                    Pair(
                        currentPath.first.plus(it.name),
                        currentPath.second + ((minutesRemaining - (distanceMap.distanceBetween(
                            pathHead.name,
                            it.name
                        ) + 1)) * it.flowRate)
                    ),
                    minutesRemaining - (distanceMap.distanceBetween(pathHead.name, it.name) + 1)
                ).plus(setOf(currentPath))
            }.toSet()
        } else setOf(currentPath)
    }

    val allPaths = getAllPaths(startingSpace, Pair(emptyList(), 0), time)
    permutations.addAll(allPaths)

    return permutations
}

private fun List<Pair<String, List<Pair<String, Int>>>>.distanceBetween(source: String, destination: String): Int {
    return find { key -> key.first == source }?.second?.find { it.first == destination }?.second!!
}

data class Valve(val name: String, val flowRate: Int, val tunnels: List<String>) {
    fun getDistancesToOthers(map: Map<String, Valve>): List<Pair<String, Int>> {
        return dijkstra(map, name)
    }
}

fun dijkstra(valves: Map<String, Valve>, start: String): List<Pair<String, Int>> {
    val dists = valves.keys.associateWith { k -> if(k == start) 0 else Int.MAX_VALUE }.toMutableMap()
    val unvisited = PriorityQueue<Pair<String, Int>>{ v1, v2 -> v1.second - v2.second}
    unvisited.add(Pair(start, 0))
    while(unvisited.isNotEmpty()) {
        val node = unvisited.poll()
        val dist = dists[node.first]!!
        valves[node.first]?.tunnels?.forEach{ tunnel ->
            if(dist + 1 < dists[tunnel]!!) {
                dists[tunnel] = dist + 1
                unvisited.add(Pair(tunnel, dist+1))
            }
        }
    }

    return dists.minus(start).map { Pair(it.key, it.value)}
}