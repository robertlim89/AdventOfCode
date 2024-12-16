fun main(args: Array<String>) {
    Day15(args[0]).run()
}

class Day15(filename: String): BaseDay(filename) {
    private val walls = mutableListOf<Coord>()
    private val boxes = mutableListOf<Coord>()
    private var robot = Coord(0,0)
    private val deltas = ArrayDeque<Coord>()

    private fun process() {
        var isMapping = true
        lines.forEachIndexed { y, line ->
            if (line.isEmpty()) {
                isMapping = false
            } else {
                line.forEachIndexed { x, c ->
                    if (isMapping) {
                        when (c) {
                            '#' -> walls.add(Coord(x, y))
                            'O' -> boxes.add(Coord(x, y))
                            '@' -> robot = Coord(x, y)
                        }
                    } else {
                        when (c) {
                            '<' -> deltas.add(Coord(-1, 0))
                            '>' -> deltas.add(Coord(1, 0))
                            '^' -> deltas.add(Coord(0, -1))
                            'v' -> deltas.add(Coord(0, 1))
                        }
                    }
                }
            }
        }
    }

    override fun part1(): String {
        process()
        while(deltas.isNotEmpty()) {
            val move = deltas.removeFirst()
            if (!walls.contains(robot.plus(move))) {
                val next = robot.plus(move)
                var boxIdx = boxes.indexOf(next)
                var nextBoxPos = next.plus(move)
                val newBoxes = mutableListOf<Coord>()
                while(boxIdx != -1 && !walls.contains(nextBoxPos)) {
                    boxes.removeAt(boxIdx)
                    newBoxes.add(nextBoxPos)
                    nextBoxPos = nextBoxPos.plus(move)
                    boxIdx = boxes.indexOf(nextBoxPos)
                }
                boxes.addAll(newBoxes)
                robot = next
            }
        }
        return boxes.sumOf { it.toGPS() }.toString()
    }

    private fun Coord.toGPS(): Long {
        return 100*y + 4*x
    }
}