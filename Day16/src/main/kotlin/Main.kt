
import java.io.File
import kotlin.math.min

fun main(args: Array<String>) {
    solve(args[0], args[1].toInt())
}

fun solve(filename: String, time: Int) {
    val regex = """\d+""".toRegex()
    val regex2 = """([A-Z]{2})""".toRegex()
    val valves = mutableMapOf<String, Valve>()
    var startPipe = ""
    File(filename).forEachLine { line ->
        val flow = regex.find(line)?.groupValues?.get(0)?.toInt()
        val pipes = regex2.findAll(line).map {it.value}.toList()
        flow?.let { valves[pipes[0]] = Valve(pipes[0], it, pipes.drop(1)) }
        startPipe = startPipe.ifEmpty { pipes[0] }
    }

    val parentMap = prims(valves, startPipe)
    println(dists)

    val totalPressure = 0
    val openValves = mutableListOf<String>()
    val moveQueue = ArrayDeque<String>()
    (1..30).forEach { time ->
        val startValve = valves[startPipe]!!
        if(moveQueue.isNotEmpty()) {
            startPipe = moveQueue.removeFirst()
        }
        else if(startValve.flowRate == 0 || openValves.contains(startPipe)) {
            val maxValue = parentMap.entries.filter { !openValves.contains(it.key) }. {

            }
        }
    }

}

data class Valve(val name: String, val flowRate: Int, val tunnels: List<String>)

fun prims(valves: MutableMap<String, Valve>, start: String): MutableMap<String, MutableList<String>> {
    val dists = valves.keys.associateWith { k -> if(k == start) 0 else Int.MAX_VALUE }.toMutableMap()
    val parentMap = valves.keys.associateWith { mutableListOf<String>() }.toMutableMap()
    val unvisited = valves.keys.toMutableList()
    while(unvisited.isNotEmpty()) {
        val node = unvisited.removeFirst()
        val dist = dists[node]!!
        val parents = parentMap[node]!!
        valves[node]?.tunnels?.forEach{ tunnel ->
            if(dist + 1 < dists[tunnel]!!) {
                dists[tunnel] = dist + 1
                parentMap[tunnel]!!.clear()
                parentMap[tunnel]!!.addAll(parents)
                parentMap[tunnel]!!.add(node)
            }
        }
    }

    return parentMap
}