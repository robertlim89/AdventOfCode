fun main(args: Array<String>) {
    Day4(args[0]).run()
}

class Day4(filename: String): BaseDay(filename) {
    override fun part1(): String {
        var count = 0
        lines.forEachIndexed { row, line ->
            line.forEachIndexed { col, c ->
                if (c == 'X') count+= search(Coord(col, row), 'M')
            }
        }
        return count.toString()
    }

    fun search(start: Coord, searchLetter: Char, dir: Coord? = null): Int {
        dir?.let{ d ->
            val new = start.plus(d)
            if (new.x < 0 || new.x >= lines[0].length || new.y < 0 || new.y >= lines.size) {
                return 0
            }
            if (lines[new.y.toInt()][new.x.toInt()] == searchLetter) {
                if (searchLetter == 'S') return 1

                return search(new, searchLetter.nextLetter(), d)
            }
            return 0
        }
        return start.directNeighbours().sumOf { nb ->
            if (nb.x >= 0 && nb.y >= 0 && nb.x < lines[0].length && nb.y < lines.size && lines[nb.y.toInt()][nb.x.toInt()] == searchLetter) {
                val d = nb.minus(start)
                val s = search(nb, searchLetter.nextLetter(), d)
                //if (s >= 0) println("$start has XMAS in $d dir")
                return@sumOf s
            }
            0
        }
    }

    override fun part2(): String {
        var count = 0
        lines.forEachIndexed { row, line ->
            line.forEachIndexed { col, c ->
                if (c == 'A' && row > 0 && col > 0 && row < lines.size-1 && col < line.length-1) {

                    /*
                    M   M
                      A
                    S   S
                    */
                    if (lines[row-1][col-1] == 'M'
                        && lines[row-1][col+1] == 'M'
                        && lines[row+1][col-1] == 'S'
                        && lines[row+1][col+1] == 'S') {
                        //println("X-MAS at [$row, $col]")
                        count++
                    }
                    /*
                    S   M
                      A
                    S   M
                    */
                    if (lines[row-1][col-1] == 'S'
                        && lines[row-1][col+1] == 'M'
                        && lines[row+1][col-1] == 'S'
                        && lines[row+1][col+1] == 'M') {
                        //println("X-MAS at [$row, $col]")
                        count++
                    }
                    /*
                    M   S
                      A
                    M   S
                    */
                    if (lines[row-1][col-1] == 'M'
                        && lines[row-1][col+1] == 'S'
                        && lines[row+1][col-1] == 'M'
                        && lines[row+1][col+1] == 'S') {
                        //println("X-MAS at [$row, $col]")
                        count++
                    }
                    /*
                    S   S
                      A
                    M   M
                    */
                    if (lines[row-1][col-1] == 'S'
                        && lines[row-1][col+1] == 'S'
                        && lines[row+1][col-1] == 'M'
                        && lines[row+1][col+1] == 'M') {
                        //println("X-MAS at [$row, $col]")
                        count++
                    }
                }
            }
        }
        return count.toString()
    }

    fun Char.nextLetter(): Char {
        return when(this) {
            'X' -> 'M'
            'M' -> 'A'
            'A' -> 'S'
            else -> throw RuntimeException()
        }
    }
}