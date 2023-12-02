import java.io.File

fun main(args: Array<String>) {
    val base = Config(12, 13, 14)
    var totalOne = 0L
    var totalTwo = 0L
    var index = 1
    File(args[0]).forEachLine { line ->
        var games = line.split(": ")[1].split("; ")
        var maxRed = 0
        var maxGreen = 0
        var maxBlue = 0
        var isPossible = true
        games.forEach { game ->
            var colors = game.split(", ")
            var red = 0
            var green = 0
            var blue = 0
            colors.forEach {
                val details = it.split(" ")
                when(details[1]) {
                    "blue" -> blue = details[0].toInt()
                    "red" -> red = details[0].toInt()
                    "green" -> green = details[0].toInt()
                }
            }
            if(red > maxRed) maxRed = red
            if(blue > maxBlue) maxBlue = blue
            if(green > maxGreen) maxGreen = green
            isPossible = isPossible && base.isPossible(Config(red, green, blue))
        }
        println("Multiplying $maxRed * $maxGreen * $maxBlue = ${maxRed * maxGreen * maxBlue}")
        totalTwo += maxRed * maxGreen * maxBlue
        if(isPossible) {
            println("Game $index is possible")
            totalOne += index
        }
        index++
    }
    println("Total One $totalOne, Total two $totalTwo")
}

data class Config(val red: Int, val green: Int, val blue: Int) {
    fun isPossible(other: Config): Boolean {
        if(other.red > red) return false
        if(other.green > green) return false
        if(other.blue > blue) return false
        return true
    }
}