import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>) {
    solve(args[0], args[1].toInt())
}

fun solve(filename: String, part: Int) {
    val regex = """-?\d+""".toRegex()
    var (minX, minY, minZ) = Array(3) { Int.MAX_VALUE }
    var (maxX, maxY, maxZ) = Array(3) { Int.MIN_VALUE }
    val cubes = File(filename).readLines().map { line ->
        val ords = regex.findAll(line).map { it.value.toInt() }.toList()
        minX = min(minX, ords[0])
        minY = min(minY, ords[1])
        minZ = min(minZ, ords[2])
        maxX = max(maxX, ords[0])
        maxY = max(maxY, ords[1])
        maxZ = max(maxZ, ords[2])
        Coord3D(ords[0], ords[1], ords[2])

    }.toSet()

    when (part) {
        1 -> {
            val freeFaces = cubes.sumOf { cube ->
                println("Cube $cube")
                6 - cubes.filter { it != cube }.count {
                    cube.isNeighbour(it)
                }
            }
            println(freeFaces)
        }

        2 -> {
            val surrounding = getSurrounding(cubes)
            val externalFaces =
                println(externalFaces)
        }
    }

}

fun floodFill() {

}

