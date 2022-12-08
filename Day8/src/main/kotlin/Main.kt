import java.io.File
import kotlin.math.max

fun main(args: Array<String>) {
    val lines = mutableListOf<List<Int>>()
    File(args[0]).forEachLine { line ->
        lines.add(line.map { c -> c.code - '0'.code })
    }
    var maxScore = 0
    for((row, trees) in lines.withIndex()) {
        for((col, tree) in trees.withIndex()) {
            var score = 0
            var cumulative = 1
            // Look left
            trees.subList(0, col).reversed().firstOrNull{
                score++
                it >= tree
            }
            cumulative *= score
            score = 0
            println("Score for left [$row][$col]=$score")
            // Look Right
            trees.subList(col+1, trees.size).firstOrNull {
                score++
                it >= tree
            }
            cumulative *= score
            score = 0
            println("Score for right [$row][$col]=$score")
            // Look up
            lines.subList(0, row).reversed().firstOrNull {
                score++
                it[col] >= tree
            }
            cumulative *= score
            score = 0
            println("Score for up [$row][$col]=$score")
            lines.subList(row+1, lines.size).firstOrNull {
                score++
                it[col] >= tree
            }
            cumulative *= score
            println("Score for down/all [$row][$col]=$cumulative\n")
            maxScore = max(cumulative, maxScore)
        }
    }
    println("$maxScore")

}