import java.io.File

fun main(args: Array<String>) {
    val mirrors = mutableMapOf<Coord, Char>()
    val lines = File(args[0]).readLines()
    lines.forEachIndexed { row, line ->
        line.forEachIndexed { col, c ->
            if (c != '.') {
                mirrors[Coord(col, row)] = c
            }
        }
    }
    val sizeY = lines.size
    val sizeX = lines[0].length

    val options = mutableListOf<Light>()
    println("[x,y] = [$sizeX, $sizeY]")
    options.addAll((0 until sizeX).map { Light(Coord(it, 0), Direction.Down) })
    options.addAll((0 until sizeX).map { Light(Coord(it, sizeY - 1), Direction.Up) })
    options.addAll((0 until sizeY).map { Light(Coord(0, it), Direction.Right) })
    options.addAll((0 until sizeY).map { Light(Coord(sizeX - 1, it), Direction.Left) })

    val maxSteps = options.maxOf { option ->
        val energised = mutableSetOf<Light>()
        val photons = ArrayDeque<Light>()
        photons.add(option)
        while (photons.isNotEmpty()) {
            val photon = photons.removeFirst()
            if (energised.add(photon)) {
                mirrors.getOrDefault(photon.position, '.')
                    .hit(photon.direction)
                    .map { Light(photon.position, it) }
                    .forEach {
                        val newPos = it.step()
                        if (newPos.x in (0 until sizeX) && newPos.y in (0 until sizeY)) {
                            photons.add(Light(newPos, it.direction))
                        }
                    }
            }
        }
        energised.map { it.position }.toSet().size
    }
    println(maxSteps)
}

data class Light(val position: Coord, var direction: Direction) {
    fun step(): Coord {
        return when (direction) {
            Direction.Up -> Coord(position.x, position.y - 1)
            Direction.Down -> Coord(position.x, position.y + 1)
            Direction.Left -> Coord(position.x - 1, position.y)
            Direction.Right -> Coord(position.x + 1, position.y)
        }
    }
}

fun Char.hit(dir: Direction): List<Direction> {
    return when (dir) {
        Direction.Up -> {
            when (this) {
                '|' -> listOf(Direction.Up)
                '-' -> listOf(Direction.Left, Direction.Right)
                '/' -> listOf(Direction.Right)
                '\\' -> listOf(Direction.Left)
                else -> listOf(Direction.Up)
            }
        }

        Direction.Down -> {
            when (this) {
                '|' -> listOf(Direction.Down)
                '-' -> listOf(Direction.Left, Direction.Right)
                '/' -> listOf(Direction.Left)
                '\\' -> listOf(Direction.Right)
                else -> listOf(Direction.Down)
            }
        }

        Direction.Left -> {
            when (this) {
                '|' -> listOf(Direction.Up, Direction.Down)
                '-' -> listOf(Direction.Left)
                '/' -> listOf(Direction.Down)
                '\\' -> listOf(Direction.Up)
                else -> listOf(Direction.Left)
            }
        }

        Direction.Right -> {
            when (this) {
                '|' -> listOf(Direction.Up, Direction.Down)
                '-' -> listOf(Direction.Right)
                '/' -> listOf(Direction.Up)
                '\\' -> listOf(Direction.Down)
                else -> listOf(Direction.Right)
            }
        }
    }
}


enum class Direction {
    Up,
    Down,
    Left,
    Right,
}