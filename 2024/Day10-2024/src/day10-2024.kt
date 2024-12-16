fun main(args: Array<String>) {
    Day10(args[0]).run()
}

class Day10(filename: String): BaseDay(filename) {
    private val map = mutableMapOf<Coord, Int>()

    private val ends = mutableListOf<Coord>()

    private var starts = mutableListOf<Coord>()

    private fun process() {
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c.isDigit()) {
                    val height = c.digitToInt()
                    when (height) {
                        0 -> {
                            starts.add(Coord(x, y))
                        }

                        9 -> {
                            ends.add(Coord(x, y))
                        }
                    }
                    map[Coord(x, y)] = height
                } else {
                    map[Coord(x, y)] = Int.MAX_VALUE
                }
            }
        }
    }

    override fun part1(): String {
        process()
        val res =  starts
            .map { start -> ends.count { end -> pathFind(start, end, map, false, false) < Int.MAX_VALUE } }

        return res.sum().toString()
    }

    override fun part2(): String {
        process()
        val res = starts
            .map { start ->  }
    }
}