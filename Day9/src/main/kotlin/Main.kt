import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    solve(args[0], 2)
    solve(args[0], 10)
}

fun solve(filename: String, segments: Int) {
    val ropePosition = Array(segments) { Pair(0, 0) }
    val tailPositions = mutableSetOf<Pair<Int, Int>>()
    tailPositions.add(ropePosition[segments - 1])
    File(filename).forEachLine { line ->
        val headMove = line.split(' ')
        //println("Head move $line")
        repeat((1..headMove[1].toInt()).count()) {
            when (headMove[0]) {
                "R" -> ropePosition[0] = Pair(ropePosition[0].first + 1, ropePosition[0].second)
                "L" -> ropePosition[0] = Pair(ropePosition[0].first - 1, ropePosition[0].second)
                "U" -> ropePosition[0] = Pair(ropePosition[0].first, ropePosition[0].second + 1)
                "D" -> ropePosition[0] = Pair(ropePosition[0].first, ropePosition[0].second - 1)
            }
            //println("Head moved to ${ropePosition[0]}")
            (1 until segments).forEach {
                val xDiff = ropePosition[it - 1].first - ropePosition[it].first
                val yDiff = ropePosition[it - 1].second - ropePosition[it].second
                if (abs(xDiff) > 1 || abs(yDiff) > 1) {
                    val xDir = if (xDiff != 0) xDiff / abs(xDiff) else 0
                    val yDir = if (yDiff != 0) yDiff / abs(yDiff) else 0
                    ropePosition[it] = Pair(ropePosition[it].first + xDir, ropePosition[it].second + yDir)
                }
                if (it == (segments - 1)) tailPositions.add(ropePosition[segments - 1])
            }
        }
    }
    //println("Steps $tailPositions")
    println("${tailPositions.size} steps for $segments segments")
}