import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    val ropePosition = Array(10) { Pair(0,0)}
    val tailPositions = mutableSetOf<Pair<Int, Int>>()
    tailPositions.add(ropePosition[9])
    File(args[0]).forEachLine { line ->
        val headMove = line.split(' ')
        //println("Head move $line")
        (1..headMove[1].toInt()).forEach {
            when(headMove[0]) {
                "R" -> ropePosition[0] = Pair(ropePosition[0].first + 1, ropePosition[0].second)
                "L" -> ropePosition[0] = Pair(ropePosition[0].first - 1, ropePosition[0].second)
                "U" -> ropePosition[0] = Pair(ropePosition[0].first, ropePosition[0].second + 1)
                "D" -> ropePosition[0] = Pair(ropePosition[0].first, ropePosition[0].second - 1)
            }
            //println("Head moved to $headPosition")
            (1..9).forEach{
                val xDiff = ropePosition[it -1].first - ropePosition[it].first
                val yDiff = ropePosition[it -1].second - ropePosition[it].second
                if(abs(xDiff) > 1 || abs(yDiff) > 1) {
                    val xDir = if(xDiff != 0) xDiff / abs(xDiff) else 0
                    val yDir = if(yDiff != 0) yDiff / abs(yDiff) else 0
                    ropePosition[it] = Pair(ropePosition[it].first + xDir, ropePosition[it].second + yDir)
                    //println("Tail moved to $tailPosition")
                }
                if(it == 9) tailPositions.add(ropePosition[9])
            }

        }
    }
    //println("Steps $tailPositions")
    println("Steps ${tailPositions.size}")
}