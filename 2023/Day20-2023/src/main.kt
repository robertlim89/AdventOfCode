fun main(args: Array<String>) {
    Day20(args[0]).run()
}

class Day20(fileName: String) : BaseDay(fileName) {
    override fun part1(): String {
        val modules = lines.associate { line ->
            val matches = Regex("([%|&]?)(\\w+) -> (.*)").matchEntire(line)
            matches?.groupValues?.let { gp ->
                val type = if (gp[1].isEmpty()) 'b' else gp[1][0]
                gp[2] to Module(type, gp[2], gp[3].split(", "))
            } ?: throw IllegalArgumentException()
        }

        val conjunctions = modules.values.filter { it.type == '&' }.map { it.name }
        modules.values.forEach { m ->
            m.outputs.forEach{ o ->
                if(o in conjunctions) {
                    modules[o]!!.act(m.name, false)
                }
            }
        }

        var high = 0L
        var low = 0L
        (1..1000).forEach {
            val queue = ArrayDeque<Flow>()
            queue.add(Flow(false, "broadcaster", "button"))
            //println("button -low-> broadcaster")
            low++
            var output = false
            while (queue.isNotEmpty()) {
                val flow = queue.removeFirst()
                if (flow.node == "rx") {
                    output = flow.state
                } else {
                    val (newState, to) = modules[flow.node]!!.act(flow.from, flow.state)
                    to.forEach { m ->
                        //println("${flow.node} -${if(newState) "high" else "low"}-> ${m}")
                        if(newState) high++ else low++
                        queue.add(Flow(newState, m, flow.node))
                    }
                }
            }
        }

        println("Output is high: $low $high")
        return (low * high).toString()
    }

    override fun part2(): String {
        val modules = lines.associate { line ->
            val matches = Regex("([%|&]?)(\\w+) -> (.*)").matchEntire(line)
            matches?.groupValues?.let { gp ->
                val type = if (gp[1].isEmpty()) 'b' else gp[1][0]
                gp[2] to Module(type, gp[2], gp[3].split(", "))
            } ?: throw IllegalArgumentException()
        }

        val conjunctions = modules.values.filter { it.type == '&' }.map { it.name }
        modules.values.forEach { m ->
            m.outputs.forEach{ o ->
                if(o in conjunctions) {
                    modules[o]!!.act(m.name, false)
                }
            }
        }

        var high = 0L
        var low = 0L
        var firstLow = Int.MAX_VALUE
        (1L..1000000000L).forEach {
            val queue = ArrayDeque<Flow>()
            queue.add(Flow(false, "broadcaster", "button"))
            //println("button -low-> broadcaster")
            low++
            var output = false
            while (queue.isNotEmpty()) {
                val flow = queue.removeFirst()
                if (flow.node == "rx") {
                    if(!flow.state) return it.toString()
                } else {
                    val (newState, to) = modules[flow.node]!!.act(flow.from, flow.state)
                    to.forEach { m ->
                        //println("${flow.node} -${if(newState) "high" else "low"}-> ${m}")
                        if(newState) high++ else low++
                        queue.add(Flow(newState, m, flow.node))
                    }
                }
            }
        }
        return Int.MAX_VALUE.toString()
    }
    data class Flow(val state: Boolean, val node: String, val from: String)

    data class Module(val type: Char, val name: String, val outputs: List<String>) {
        private val inputMap = mutableMapOf<String, Boolean>().withDefault { false }

        private var state = false
        fun act(from: String, isHighPulse: Boolean): Pair<Boolean, List<String>> {
            return when (type) {
                'b' -> isHighPulse to outputs
                '%' -> {
                    if(!isHighPulse) {
                        state = !state
                        return state to outputs
                    }
                    return true to listOf()
                }
                '&' -> {
                    inputMap[from] = isHighPulse
                    inputMap.values.any { !it } to outputs
                }

                else -> throw IllegalArgumentException()
            }
        }
    }


}