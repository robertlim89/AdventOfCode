import java.io.File
import java.math.BigDecimal
import kotlin.math.abs

fun main(args: Array<String>) {
    Day18(args[0]).run()
}

class Day18(fileName: String): BaseDay(fileName) {
    override fun part1(): String {
        var posX = 0
        var posY = 0
        val vertices = mutableListOf(Coord(0, 0))
        var perimeter = 0
        lines.forEach { line ->
            val props = line.split(' ')
            val length = props[1].toInt()
            when (props[0]) {
                "R" -> {
                    posX += length
                }

                "L" -> {
                    posX -= length
                }

                "U" -> {
                    posY -= length
                }

                "D" -> {
                    posY += length
                }
            }
            perimeter += length
            vertices.add(Coord(posX, posY))
        }
        var area = 0.0
        vertices.indices.forEach {
            area += vertices[it].x * vertices[(it + 1) % vertices.size].y -
                    vertices[(it + 1) % vertices.size].x * vertices[it].y
        }
        println(perimeter)
        area = (abs(area) + perimeter) /2 + 1
        return area.toInt().toString()
    }

    override fun part2(): String {
        var posX = 0L
        var posY = 0L
        val vertices = mutableListOf(LongCoord(0, 0))
        var perimeter = 0L
        lines.forEach { line ->
            val props = line.split(' ')
            val length = props[2].substring(2..6).toLong(16)
            val direction = "RDLU"[props[2][7].digitToInt()]
            when (direction) {
                'R' -> {
                    posX += length
                }

                'L' -> {
                    posX -= length
                }

                'U' -> {
                    posY -= length
                }

                'D' -> {
                    posY += length
                }
            }
            println("Instruction [$direction, $length")
            perimeter += length
            vertices.add(LongCoord(posX, posY))
        }
        var area =  0L
        vertices.indices.forEach {
            area += 1L * vertices[it].x * vertices[(it + 1) % vertices.size].y
            area -= 1L * vertices[(it + 1) % vertices.size].x * vertices[it].y
        }
        println(perimeter)
        return ((area + perimeter)/2L + 1L).toString()
    }
}

