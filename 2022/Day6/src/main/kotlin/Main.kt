import java.io.File

fun main(args: Array<String>) {
    File(args[0]).forEachLine { line ->
        var start = 0
        var found = false
        while(!found && (start + 14) < line.length) {
            val chars = line.substring(start, start + 14).toSet()
            if (chars.size == 14) {
                println("Found at ${start + 14}")
                found = true
            }
            start++
        }
    }
}