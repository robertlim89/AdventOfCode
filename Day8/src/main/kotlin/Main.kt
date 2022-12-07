import java.io.File

fun main(args: Array<String>) {
    File(args[0]).forEachLine { line ->
        println(line)
    }
}