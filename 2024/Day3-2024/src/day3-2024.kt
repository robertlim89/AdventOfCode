fun main(args: Array<String>) {
    Day3(args[0]).run()
}

class Day3(filename: String): BaseDay(filename) {
    override fun part1(): String {
        return lines.sumOf { line ->
            "(mul\\(\\d+,\\d+\\))".toRegex().findAll(line)
                .map { it.groupValues.first() }
                .map { Mul.fromString(it) }
                .sumOf { it.toValue()}
        }.toString()
    }

    override fun part2(): String {
        val line =  lines.joinToString()
        val rep = line.replace("""don't\(\).*?(?:do\(\)|${'$'})""".toRegex(RegexOption.DOT_MATCHES_ALL), "")
        return "(mul\\(\\d+,\\d+\\))".toRegex().findAll(rep)
            .map { it.groupValues.first() }
            .map { Mul.fromString(it) }
            .sumOf { it.toValue() }
            .toString()
    }

    data class Mul(val a: Long, val b: Long) {
        fun toValue() : Long {
            val value = a.times(b)
            //println("$a x $b = $value")
            return value
        }
        companion object {
            fun fromString(line: String): Mul {
                val vals = """mul\((\d+),(\d+)\)""".toRegex().find(line)
                val args = vals?.groupValues?.drop(1)?.map { it.toLong() }
                return Mul(args?.get(0)!!, args[1])
            }
        }
    }
}