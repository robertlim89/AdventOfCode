import java.io.File
import kotlin.math.max

fun main(args: Array<String>) {
    val lines = File(args[0]).readLines().map(String::toCharArray).map{ it.map(Char::digitToInt)}
    var maxScore = 0
    for ((row, trees) in lines.withIndex()) {
        for ((col, tree) in trees.withIndex()) {
            var score = 0
            var cumulative = 1
            // Look left
            trees.subList(0, col).reversed().firstOrNull {
                score++
                it >= tree
            }
            cumulative *= score

            println("Score for left [$row][$col]=$score")
            // Look Right
            score = 0
            trees.subList(col + 1, trees.size).firstOrNull {
                score++
                it >= tree
            }
            cumulative *= score

            println("Score for right [$row][$col]=$score")
            score = 0
            // Look up
            lines.subList(0, row).reversed().firstOrNull {
                score++
                it[col] >= tree
            }
            cumulative *= score
            println("Score for up [$row][$col]=$score")
            score = 0
            lines.subList(row + 1, lines.size).firstOrNull {
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