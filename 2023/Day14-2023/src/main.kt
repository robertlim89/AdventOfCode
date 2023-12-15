import java.io.File

fun main(args: Array<String>) {
    var row = 0
    var rocks = mutableListOf<Coord>()
    val boulders = mutableListOf<Coord>()
    var size = 0
    File(args[0]).forEachLine { line ->
        line.forEachIndexed { col, c ->
            when(c) {
                'O' -> rocks.add(Coord(col, row))
                '#' -> boulders.add(Coord(col, row))
            }
        }
        size = line.length
        row++
    }

    val seenCycles = mutableSetOf<Int>()
    val directions = arrayOf(Direction.North, Direction.West, Direction.South, Direction.East)
    var cycleSyncNum: Int? = null
    var cycleStart = -1
    repeat(100000){ cycle ->
        val direction = directions[cycle % directions.size]
        rocks = rocks.tilt(boulders, direction, size, row)
        val sum = rocks.sumOf{ row-it.y }
        if(cycle % 4 == 3) {
            if(cycleSyncNum == sum) {
                val cycleLength = cycle - cycleStart
                val remainingChanges = args[1].toLong() * 4 - cycleStart
                val remaingCycles = remainingChanges / cycleLength
                val extraChanges = (remainingChanges % cycleLength).toInt()
                println("Direction change $cycle: sum=$sum cycleLength= $cycleLength, cyclesToJump = $remainingChanges " +
                        "remaingCycles =$remaingCycles extraChanges=$extraChanges")
                println("sum = ${seenCycles}")
                return
            }
            if(cycleSyncNum == null) {
                if(!seenCycles.add(sum)) {
                    cycleSyncNum = sum
                    cycleStart = cycle
                    println("Catching cycle at $sum on $cycleStart")
                }
            }
        }
    }
}

fun List<Coord>.tilt(boulders: List<Coord>, direction: Direction, sizeX: Int, sizeY: Int): MutableList<Coord> {
    val newRocks = mutableListOf<Coord>()
    val rocks = if(direction in listOf(Direction.South, Direction.East)) this.reversed() else this
    for(rock in rocks) {
        var newX = rock.x
        var newY = rock.y
        when(direction) {
            Direction.North -> {
                val northBoulder = boulders.filter{ direction.getBoulderFilter().invoke(it, rock) }.maxOfOrNull { it.y } ?: -1
                val northRock = newRocks.filter{direction.getBoulderFilter().invoke(it, rock)}.maxOfOrNull { it.y } ?: -1
                newY = kotlin.math.max(northBoulder, northRock) + 1
            }
            Direction.East -> {
                val northBoulder = boulders.filter{ direction.getBoulderFilter().invoke(it, rock) }.minOfOrNull { it.x } ?: sizeX
                val northRock = newRocks.filter{direction.getBoulderFilter().invoke(it, rock)}.minOfOrNull { it.x } ?: sizeX
                newX = kotlin.math.min(northBoulder, northRock) - 1
            }
            Direction.South -> {
                val northBoulder = boulders.filter{ direction.getBoulderFilter().invoke(it, rock) }.minOfOrNull { it.y } ?: sizeY
                val northRock = newRocks.filter{direction.getBoulderFilter().invoke(it, rock)}.minOfOrNull { it.y } ?: sizeY
                newY = kotlin.math.min(northBoulder, northRock) - 1
            }
            Direction.West -> {
                val northBoulder = boulders.filter{ direction.getBoulderFilter().invoke(it, rock) }.maxOfOrNull { it.x } ?: -1
                val northRock = newRocks.filter{direction.getBoulderFilter().invoke(it, rock)}.maxOfOrNull { it.x } ?: -1
                newX = kotlin.math.max(northBoulder, northRock) + 1
            }
        }
        newRocks.add(Coord(newX, newY))
    }
    return newRocks
}

enum class Direction{
    North,
    East,
    South,
    West;

    fun getBoulderFilter(): (Coord, Coord) -> Boolean {
        return when(this) {
            North -> {
                {it, rock -> it.y < rock.y && it.x == rock.x}
            }
            East -> {
                {it, rock -> it.y == rock.y && it.x >= rock.x }
            }
            South -> {
                {it, rock -> it.y > rock.y && it.x == rock.x }
            }
            West -> {
                {it, rock -> it.y == rock.y && it.x < rock.x }
            }
        }
    }
}

fun List<Coord>.print(boulders: List<Coord>, rows: Int, cols: Int) {
    (0 until rows).forEach { r ->
        (0 until cols).forEach{ c ->
            val coord = Coord(c, r)
            if(coord in this) {
                print('O')
            } else if(coord in boulders) {
                print('#')
            } else {
                print('.')
            }
        }
        println()
    }
}