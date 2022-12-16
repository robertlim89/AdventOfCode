
import java.io.File

fun main(args: Array<String>) {
    solve(args[0], args[1].toInt())
}

fun solve(filename: String, time: Int) {
    val regex = """\d+""".toRegex()
    val regex2 = """([A-Z]{2})""".toRegex()
    val valves = Graph<String>()
    val valveData = mutableMapOf<String, Int>()
    var currentPipe: String? = null
    File(filename).forEachLine { line ->
        val flow = regex.find(line)?.groupValues?.get(0)?.toInt()
        val pipes = regex2.findAll(line).map {it.value}.toList()
        pipes.drop(1).forEach{ p -> valves.addEdge(pipes[0], p)}
        valveData[pipes[0]] = flow!!
        currentPipe = currentPipe ?: pipes[0]
    }

    var flow = 0
    val opened = setOf<String>()

    (1..time).forEach {
        println("Minute $it")
        var openedValve: String? = null
        var newPipe: String? = null
        val currentValve = valves[currentPipe]!!
        if(!currentValve.open && currentValve.flowRate > 0) {
            openedValve = currentPipe
        }
        else {
            newPipe = currentValve.tunnels.first { pipe ->
                val newValve = valves[pipe]!!
                !newValve.open && newValve.flowRate > 0
            }
        }

        flow += valves.values.sumOf { if(it.open) it.flowRate else 0 }
        openedValve?.let{
            println("  Opened $it")
            valves[it]!!.open = true
        }
        newPipe?.let {
            println("  Moved to $newPipe")
            currentPipe = newPipe
        }
        println("Flow is $flow")
    }
}

data class Valve(val flowRate: Int)