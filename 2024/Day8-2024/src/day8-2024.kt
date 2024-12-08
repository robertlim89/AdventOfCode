fun main(args: Array<String>) {
    Day8(args[0]).run()
}

class Day8(filename: String): BaseDay(filename) {
    private val antennas = mutableMapOf<Char, MutableSet<Coord>>()
    private var bounds = Coord(0,0)

    private fun process() {
        bounds = Coord(lines[0].length, lines.size)
        lines.forEachIndexed{ y,line ->
            line.forEachIndexed { x, c ->
                when(c) {
                    '#','.' -> Unit
                    else -> {
                        antennas.computeIfAbsent(c) { mutableSetOf() }
                        antennas[c]!!.add(Coord(x,y))
                    }
                }
            }
        }
    }

    override fun part1(): String {
        process()
        val antinodes = mutableSetOf<Coord>()
        antennas.values.forEach { poses ->
            val list = poses.toList()
            list.forEachIndexed{ id, pos1 ->
                (id+1..list.size-1).forEach { id2 ->
                    val diff = list[id2].minus(pos1)
                    var neighbor = pos1.minus(diff)
                    if(neighbor.inBounds(bounds)) {
                        antinodes.add(neighbor)
                    }
                    neighbor = list[id2].plus(diff)
                    if(neighbor.inBounds(bounds)) {
                        antinodes.add(neighbor)
                    }
                }
            }
        }

        return antinodes.size.toString()
    }

    override fun part2(): String {
        process()
        val antinodes = mutableSetOf<Coord>()
        antennas.values.forEach { poses ->
            antinodes.addAll(poses)
            val list = poses.toList()
            list.forEachIndexed{ id, pos1 ->
                (id+1..list.size-1).forEach { id2 ->
                    val diff = list[id2].minus(pos1)
                    var neighbor = pos1.minus(diff)
                    while(neighbor.inBounds(bounds)) {
                        antinodes.add(neighbor)
                        neighbor = neighbor.minus(diff)
                    }
                    neighbor = list[id2].plus(diff)
                    while(neighbor.inBounds(bounds)) {
                        antinodes.add(neighbor)
                        neighbor = neighbor.plus(diff)
                    }
                }
            }
        }

        return antinodes.size.toString()
    }
}