import java.io.File
import java.lang.IllegalArgumentException

fun main(args: Array<String>) {
    solve(args[0])
}

fun solve(filename: String) {
    val numbers = File(filename).readLines().map { line ->
        line.toCharArray().fold(0L) { acc, c -> acc * 5 + c.toDecimal() }
    }
    // Uncomment below to check mapping
    //println("Is mapping equal to input = ${numbers.map { it.toSNAFU() } == File(filename).readLines()}")
    print("Sum of numbers is ${numbers.sum().toSNAFU()}")
}

fun Char.toDecimal(): Long {
    return when (this) {
        '0' -> 0
        '1' -> 1
        '2' -> 2
        '=' -> -2
        '-' -> -1
        else -> throw IllegalArgumentException("Unknown symbol: $this")
    }
}

fun Long.toSNAFU(): String {
    // Returns the new character and the net result of adding the SNAFU character
    fun Long.toSNAFUDigit(): Pair<Char, Long> {
        return when (this) {
            0L -> '0' to 0
            1L -> '1' to -1
            2L -> '2' to -2
            3L -> '=' to 2
            4L -> '-' to 1
            else -> throw IllegalArgumentException("Can't resolve: $this")
        }
    }

    var result = ""
    var sum = this
    while (sum != 0L) {
        val mod = (sum % 5).toSNAFUDigit()
        result = mod.first + result
        sum += mod.second
        sum /= 5
    }
    return result
}