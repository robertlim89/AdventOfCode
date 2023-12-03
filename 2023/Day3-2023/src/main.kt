import java.io.File

fun main(args: Array<String>) {
    val lines = File(args[0]).readLines()
    var total = 0L
    val gearMap = mutableMapOf<Coord, MutableList<Int>>()
    val directions = listOf(Pair(-1, -1), Pair(0, -1), Pair(1, -1), Pair(1, 0), Pair(1,1), Pair(0, 1), Pair(-1, 1), Pair(-1, 0))
    lines.forEachIndexed { r, line ->
        var startIndex = 0
        var isPart = false
        var gearCoord: Coord? = null
        line.forEachIndexed { c, pos ->
            if (!pos.isDigit()) {
                if(c-startIndex > 0) {
                    val value = line.substring(startIndex, c).toInt()
                    //println("value $value isPart $isPart")
                    if (isPart) {
                        total += value
                    }
                    gearCoord?.let {
                        //println("$value is part of a gear $it")
                        gearMap[it]?.add(value)
                    }
                }
                startIndex = c + 1
                isPart = false
                gearCoord = null
            } else {
                directions.forEach { d ->
                    val x = r + d.first
                    val y = c + d.second
                    if(x >= 0 && x < line.length && y >= 0 && y < lines.size) {
                        val result = !lines[x][y].isDigit() && lines[x][y] != '.'
                        //if(result) println("lines[$x][$y] has symbol ${lines[x][y]}")
                        isPart = isPart || result
                        if(lines[x][y] == '*') {
                            gearCoord = Coord(x,y)
                            gearMap.computeIfAbsent(Coord(x,y)) { mutableListOf() }
                        }
                    }
                }
            }
        }
        if(line.length-startIndex > 0) {
            val value = line.substring(startIndex, line.length).toInt()
            //println("value $value isPart $isPart")
            if (isPart) {
                total += value
            }
            gearCoord?.let {
                //println("$value is part of a gear $it")
                gearMap[it]?.add(value)
            }
        }
    }
    val gearTotal = gearMap.values.filter { it.size == 2 }.map{ it[0] * it[1].toLong() }.reduce { acc, i -> acc+i }

    println("Total $total Gear total $gearTotal")
}