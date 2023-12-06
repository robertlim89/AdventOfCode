import java.io.File
import kotlin.math.*

fun main(args: Array<String>) {
    var total = 1L
    val races = mutableListOf<Params>()
    val lines = File(args[0]).readLines()
    val times = lines[0].split("\\s+".toRegex()).filter { it.isNotBlank() }.drop(1).map{ it.toDouble() }
    val distances = lines[1].split("\\s+".toRegex()).filter { it.isNotBlank() }.drop(1).map{ it.toLong() }
    times.indices.forEach{
        races.add(Params(times[it], distances[it]))
    }
    races.filter{ it.isValid() }.forEachIndexed{ idx, param ->
        val bounds = param.getBounds()
        println("Race ${idx+1} can hold button between [$bounds] = ${bounds.second - bounds.first + 1}")
        total *= bounds.second - bounds.first + 1
    }
    println(total)
}

data class Params(val time: Double, val distance: Long) {
    fun isValid(): Boolean {
        return (time.pow(2) - 4*distance) > 0
    }

    fun getBounds(): Pair<Int, Int> {
        val lower = floor(0.5 * (time - sqrt(time.pow(2) - 4*distance)) + 1).toInt()
        val upper = ceil(0.5 * (time + sqrt(time.pow(2) - 4*distance)) - 1).toInt()
        return lower to upper
    }
}

