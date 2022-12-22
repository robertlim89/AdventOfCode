import java.io.File
import java.lang.IllegalArgumentException

fun main(args: Array<String>) {
    if (args[1].equals("1")) {
        solvePart1(args[0])
    } else {
        solvePart2(args[0], args[2].toInt(), args[0].contains("sample"))
    }
}

fun solvePart1(filename: String) {
    val map = mutableMapOf<Int, Map<Int, Char>>()
    for ((index, line) in File(filename).readLines().withIndex().takeWhile { it.value.isNotEmpty() }) {
        map[index + 1] =
            line.toCharArray().withIndex().filter { it.value != ' ' }.associate { it.index + 1 to it.value }
    }
    val regex = """[RL]|\d+""".toRegex()
    val directions = regex.findAll(File(filename).readLines().takeLast(1)[0]).map { it.value }.toList()
    println(directions)
    var playerCoord = Coord(map[1]!!.keys.first(), 1)
    println(playerCoord)
    var playerDirection = Direction.RIGHT
    for (newDirection in directions) {
        val asSteps = newDirection.toIntOrNull()
        if (asSteps != null) {
            // Make that many steps
            val steps = playerDirection.getOffset()
            //println(map[playerCoord.y])
            for (s in 1..asSteps) {
                val row = map[playerCoord.y]!!
                val column = map.filter { it.value.containsKey(playerCoord.x) }.mapValues { it.value[playerCoord.x] }
                val newX = if ((playerCoord.x + steps.first) > row.keys.last()) row.keys.first()
                else if ((playerCoord.x + steps.first) < row.keys.first()) row.keys.last()
                else playerCoord.x + steps.first
                val newY = if ((playerCoord.y + steps.second) > column.keys.last()) column.keys.first()
                else if ((playerCoord.y + steps.second) < column.keys.first()) column.keys.last()
                else playerCoord.y + steps.second
                //println("Going from $playerCoord to [$newX,$newY]")
                //println("${map[newY]!![newX]}")
                if (map[newY]?.get(newX) != '#') {
                    playerCoord = Coord(newX, newY)
                } else { //println("Stop")
                    break
                }
            }
            //println("Stopped at $playerCoord")
        } else {
            playerDirection = playerDirection.turn(newDirection == "R")
            //println("Turned $newDirection to $playerDirection")
        }
    }
    println(
        "Player is at $playerCoord and Direction is $playerDirection: P/W = ${
            generatePassword(
                playerCoord,
                playerDirection
            )
        }"
    )
}

fun solvePart2(filename: String, faceSize: Int, isSample: Boolean) {
    val map = Array(6) { mutableMapOf<Int, Map<Int, Char>>() }
    var faceIndex = 0
    for ((index, line) in File(filename).readLines().withIndex().takeWhile { it.value.isNotEmpty() }) {
        val tiles = line.toCharArray().withIndex().filter { it.value != ' ' }
        for (space in tiles.indices step faceSize) {
            val newTiles = tiles.drop(space).take(faceSize).associate { (it.index % faceSize) + 1 to it.value }
            if(newTiles.isNotEmpty()) {
                map[faceIndex + (space / faceSize)][(index % faceSize)+1] = newTiles
            }
        }
        if (map[faceIndex].size == faceSize) {
            faceIndex += tiles.size / faceSize
        }
    }
    val regex = """[RL]|\d+""".toRegex()
    val directions = regex.findAll(File(filename).readLines().takeLast(1)[0]).map { it.value }.toList()
    var playerCoord = Coord3D(map[0].keys.first(), 1, 0)
    println(playerCoord)
    var playerDirection = Direction.RIGHT
    println(playerCoord)

    for (newDirection in directions) {
        val asSteps = newDirection.toIntOrNull()
        if (asSteps != null) {
            //println("Going $playerDirection $asSteps from ${playerCoord.getNormalisedPosition(isSample)} ")
            // Make that many steps
            //println(map[playerCoord.y])
            for (s in 1..asSteps) {
                val (newPos, newDir) = playerCoord.getNextPosition(playerDirection, faceSize, isSample)
                if (map[newPos.z][newPos.y]?.get(newPos.x) != '#') {
                    //println("Moved to $newPos (${newPos.getNormalisedPosition(isSample)} going $newDir")
                    playerCoord = newPos
                    playerDirection = newDir
                } else {
                    //println("Stop")
                    break
                }
            }
            //println("Stopped at $playerCoord or ${playerCoord.getNormalisedPosition(isSample)}")

        } else {
            playerDirection = playerDirection.turn(newDirection == "R")
            //println("Turned $newDirection to $playerDirection")
        }
    }

    println(
        "Player is at $playerCoord or ${playerCoord.getNormalisedPosition(isSample)} and Direction is $playerDirection: P/W = ${
            generatePassword(
                playerCoord,
                playerDirection, isSample
            )
        }"
    )
}

fun generatePassword(coord: Coord, direction: Direction): Int = 1000 * coord.y + 4 * coord.x + direction.value

fun generatePassword(coord: Coord3D, direction: Direction, isSample: Boolean): Int {
    val (x, y) = coord.getNormalisedPosition(isSample)
    return 1000 * y + 4 * x + direction.value
}


fun Coord3D.getNormalisedPosition(isSample: Boolean): Pair<Int, Int> {
    return if(isSample) {
        when(this.z) {
            0 -> Pair(this.x+8, this.y)
            1 -> Pair(this.x, this.y+4)
            2 -> Pair(this.x+4, this.y+4)
            3 -> Pair(this.x+8, this.y+4)
            4 -> Pair(this.x+8, this.y+8)
            5 -> Pair(this.x+12, this.y+8)
            else -> throw IllegalArgumentException("Unrecognised face ${this.z}")
        }
    } else {
        when(this.z) {
            0 -> Pair(this.x + 50, this.y)
            1 -> Pair(this.x + 100, this.y)
            2 -> Pair(this.x + 50, this.y + 50)
            3 -> Pair(this.x, this.y + 100)
            4 -> Pair(this.x + 50, this.y + 100)
            5 -> Pair(this.x, this.y + 150)
            else -> throw IllegalArgumentException("Unrecognised face ${this.z}")
        }
    }
}

fun Coord3D.getNextPosition(direction: Direction, faceSize: Int, isSample: Boolean): Pair<Coord3D, Direction> {
    val offset = direction.getOffset()
    val newX = x + offset.first
    val newY = y + offset.second
    if (newX in 1..faceSize && newY in 1..faceSize) {
        return Pair(Coord3D(newX, newY, z), direction)
    }
    
    return if(isSample) {
        when (z) {
            0 -> when (direction) {
                Direction.RIGHT -> Pair(Coord3D(faceSize, faceSize + 1 - y, 5), Direction.LEFT)
                Direction.DOWN -> Pair(Coord3D(x, 1, 3), Direction.DOWN)
                Direction.LEFT -> Pair(Coord3D(y, 1, 2), Direction.DOWN)
                Direction.UP -> Pair(Coord3D(x, faceSize, 4), Direction.UP)
            }

            1 -> when (direction) {
                Direction.RIGHT -> Pair(Coord3D(1, y, 2), Direction.RIGHT)
                Direction.DOWN -> Pair(Coord3D(faceSize + 1 - x, faceSize, 4), Direction.UP)
                Direction.LEFT -> Pair(Coord3D(y, faceSize, 5), Direction.UP)
                Direction.UP -> Pair(Coord3D(faceSize + 1 - x, 1, 0), Direction.DOWN)
            }

            2 -> when (direction) {
                Direction.RIGHT -> Pair(Coord3D(1, y, 3), Direction.RIGHT)
                Direction.DOWN -> Pair(Coord3D(1, faceSize + 1 - x, 4), Direction.RIGHT)
                Direction.LEFT -> Pair(Coord3D(faceSize, y, 1), Direction.LEFT)
                Direction.UP -> Pair(Coord3D(1, x, 0), Direction.RIGHT)
            }

            3 -> when (direction) {
                Direction.RIGHT -> Pair(Coord3D(faceSize + 1 - y, 1, 5), Direction.DOWN)
                Direction.DOWN -> Pair(Coord3D(x, 1, 4), Direction.DOWN)
                Direction.LEFT -> Pair(Coord3D(faceSize, y, 2), Direction.LEFT)
                Direction.UP -> Pair(Coord3D(x, faceSize, 0), Direction.UP)
            }

            4 -> when (direction) {
                Direction.RIGHT -> Pair(Coord3D(1, y, 5), Direction.RIGHT)
                Direction.DOWN -> Pair(Coord3D(faceSize + 1 - x, faceSize, 1), Direction.UP)
                Direction.LEFT -> Pair(Coord3D(faceSize + 1 - x, faceSize, 2), Direction.UP)
                Direction.UP -> Pair(Coord3D(x, faceSize, 3), Direction.UP)
            }

            5 -> when (direction) {
                Direction.RIGHT -> Pair(Coord3D(faceSize, faceSize + 1 - y, 0), Direction.LEFT)
                Direction.DOWN -> Pair(Coord3D(1, faceSize + 1 - x, 1), Direction.RIGHT)
                Direction.LEFT -> Pair(Coord3D(faceSize, y, 4), Direction.LEFT)
                Direction.UP -> Pair(Coord3D(faceSize, faceSize + 1 - x, 3), Direction.LEFT)
            }

            else -> throw IllegalArgumentException("Unrecognised face $z")
        }
    } else {
        when (z) {
            0 -> when (direction) {
                Direction.RIGHT -> Pair(Coord3D(1, y, 1), Direction.RIGHT)
                Direction.DOWN -> Pair(Coord3D(x, 1, 2), Direction.DOWN)
                Direction.LEFT -> Pair(Coord3D(1, faceSize + 1 - y, 3), Direction.RIGHT)
                Direction.UP -> Pair(Coord3D(1, x, 5), Direction.RIGHT)
            }

            1 -> when (direction) {
                Direction.RIGHT -> Pair(Coord3D(faceSize, faceSize+1-y, 4), Direction.LEFT)
                Direction.DOWN -> Pair(Coord3D(faceSize, x, 2), Direction.LEFT)
                Direction.LEFT -> Pair(Coord3D(faceSize, y, 0), Direction.LEFT)
                Direction.UP -> Pair(Coord3D(x, faceSize, 5), Direction.UP)
            }

            2 -> when (direction) {
                Direction.RIGHT -> Pair(Coord3D(y, faceSize, 1), Direction.UP)
                Direction.DOWN -> Pair(Coord3D(x, 1, 4), Direction.DOWN)
                Direction.LEFT -> Pair(Coord3D(y, 1, 3), Direction.DOWN)
                Direction.UP -> Pair(Coord3D(x, faceSize, 0), Direction.UP)
            }

            3 -> when (direction) {
                Direction.RIGHT -> Pair(Coord3D(1, y,4), Direction.RIGHT)
                Direction.DOWN -> Pair(Coord3D(x, 1, 5), Direction.DOWN)
                Direction.LEFT -> Pair(Coord3D(1, faceSize+1-y, 0), Direction.RIGHT)
                Direction.UP -> Pair(Coord3D(1, x, 2), Direction.RIGHT)
            }

            4 -> when (direction) {
                Direction.RIGHT -> Pair(Coord3D(faceSize, faceSize+1-y, 1), Direction.LEFT)
                Direction.DOWN -> Pair(Coord3D(faceSize, x, 5), Direction.LEFT)
                Direction.LEFT -> Pair(Coord3D(faceSize, y, 3), Direction.LEFT)
                Direction.UP -> Pair(Coord3D(x, faceSize, 2), Direction.UP)
            }

            5 -> when (direction) {
                Direction.RIGHT -> Pair(Coord3D(y, faceSize, 4), Direction.UP)
                Direction.DOWN -> Pair(Coord3D(x, 1, 1), Direction.DOWN)
                Direction.LEFT -> Pair(Coord3D(y, 1, 0), Direction.DOWN)
                Direction.UP -> Pair(Coord3D(x, faceSize, 3), Direction.UP)
            }

            else -> throw IllegalArgumentException("Unrecognised face $z")
        }
    }

}

enum class Direction(val value: Int) {
    RIGHT(0),
    DOWN(1),
    LEFT(2),
    UP(3);

    fun getOffset(): Pair<Int, Int> = when (this) {
        RIGHT -> Pair(1, 0)
        LEFT -> Pair(-1, 0)
        UP -> Pair(0, -1)
        DOWN -> Pair(0, 1)
    }

    fun turn(clockwise: Boolean): Direction = when (this) {
        RIGHT -> if (clockwise) DOWN else UP
        LEFT -> if (clockwise) UP else DOWN
        UP -> if (clockwise) RIGHT else LEFT
        DOWN -> if (clockwise) LEFT else RIGHT
    }
}