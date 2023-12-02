import java.io.File
import kotlin.text.StringBuilder

fun main(args: Array<String>) {
    var total = 0L
    File(args[0]).forEachLine { line ->
        val number = StringBuilder()
        var (firstDigit, lastDigit) = line.replaceNumbers()
        number.append(firstDigit)
        number.append(lastDigit)
        println("number: ${number.toString()}")
        total += number.toString().toLong()
        number.clear()

    }
    println("total: $total")
}

fun String.replaceNumbers(): Pair<Int, Int> {
    val numbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
    var start = 0
    var end = 0
    var firstDigit = 0
    var lastDigit = 0
    var found = false
    while(!found && end <= this.lastIndex) {
        val sub= this.substring(start, end+1)
        var res = sub.findAnyOf(numbers)
        val str = res?.second ?: ""
        //println("$sub $str")
        if (this[end].isDigit()) {
            firstDigit = this[end].digitToInt()
            found = true
        } else if (res != null) {
            val num = res.second.toNumber()
            //println("Adding $num")
            firstDigit = num
            found = true
        } else {
            end++
        }
    }
    found = false
    end = this.lastIndex
    start = this.lastIndex
    while(!found && start >= 0 ) {
        val sub = this.substring(start, end+1)
        val res = sub.findAnyOf(numbers)
        val str = res?.second ?: ""
        //println("$sub $str")
        if (this[start].isDigit()) {
            lastDigit = this[start].digitToInt()
            found = true
        } else if (res != null) {
            val num = res.second.toNumber()
            //println("Adding $num")
            lastDigit = num
            found = true
        } else {
            start--
        }
    }

    return Pair(firstDigit, lastDigit)
}

fun String.toNumber(): Int {
    return listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine").indexOf(this) + 1
}