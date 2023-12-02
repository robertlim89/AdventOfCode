import java.io.File

fun main(args: Array<String>) {
    var signal = 1
    var cycle = 0
    val cycles = arrayOf(19, 59, 99, 139, 179, 219)
    var totalSignalStrength = 0
    File(args[0]).forEachLine { line ->
        var num = 0
        var doCycles = 0
        if (line == "noop") {
            // do nothing
            doCycles = 1
        } else if (line.startsWith("addx")) {
            num = line.substring(5, line.length).toInt()
            doCycles = 2
        }
        repeat((1..doCycles).count()) {
            if (cycles.contains(cycle)) {
                totalSignalStrength += (cycle + 1) * signal
            }
            val linePos = cycle % 40
            val minSprite = (signal % 40) - 1
            val maxSprite = (signal % 40) + 1
            print(if (linePos in minSprite..maxSprite) "#" else " ")
            cycle++
            if (cycle % 40 == 0) print("\n")
        }
        signal += num
    }

    println(totalSignalStrength)
}