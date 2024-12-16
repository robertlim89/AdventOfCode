fun main(args: Array<String>) {
    Day13(args[0]).run()
}

class Day13(filename: String): BaseDay(filename) {
    private val games = mutableListOf<Game>()

    private fun process() {
        var buttonA = Button(Coord(0,0,), 1)
        var buttonB = Button(Coord(0,0,), 3)
        var goal = Coord(0, 0)
        lines.forEach { line ->
            if (line.isEmpty()) {
                //println("A = $buttonA, B = $buttonB, goal = $goal")
                games.add(Game(buttonA, buttonB, goal))
            } else if (line.startsWith("Button A")) {
                val parse = "Button A: X\\+(\\d+), Y\\+(\\d+)".toRegex().find(line)
                buttonA = Button(Coord(parse!!.groupValues[1].toLong(), parse.groupValues[2].toLong()), 3)

            } else if (line.startsWith("Button B")) {
                val parse = "Button B: X\\+(\\d+), Y\\+(\\d+)".toRegex().find(line)
                buttonB = Button(Coord(parse!!.groupValues[1].toLong(), parse.groupValues[2].toLong()), 1)
            } else {
                val parse = "Prize: X=(\\d+), Y=(\\d+)".toRegex().find(line)
                goal = Coord(parse!!.groupValues[1].toLong(), parse.groupValues[2].toLong())
            }
        }
    }

    override fun part1(): String {
        process()
        return games.map { it.calculate() }.filterNotNull().sumOf { it }.toString()
    }
}

data class Game(val buttonA: Button, val buttonB: Button, val goal: Coord) {
    fun calculate(): Long? {
        val num_b = 1.0* (goal.y * buttonA.increment.x - goal.x * buttonA.increment.y) / (buttonA.increment.x * buttonB.increment.y - buttonB.increment.x * buttonA.increment.y)
        val num_a = 1.0* (goal.x - buttonB.increment.x * num_b) / buttonA.increment.x
        print("$num_a and $num_b")
        if (num_a.isLong() && num_a >= 0 && num_b.isLong() && num_b >= 0) {
            println(" is ok = ${num_a.toLong()*3 + num_b.toLong()}")
            return num_a.toLong()*3 + num_b.toLong()
        }
        println(" is not")
        return null
    }

    fun Double.isLong() : Boolean {
        return Math.floor(this) == this
    }
}

data class Button(val increment: Coord, val cost: Int)