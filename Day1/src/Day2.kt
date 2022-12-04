import domain.RPSRound
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

object Day2 {
    @JvmStatic
    fun main(args: Array<String>) {
        val games = ArrayList<RPSRound>()
        try {
            BufferedReader(FileReader(args[0])).use { fileReader ->
                var line = fileReader.readLine()
                while (line != null) {
                    val round = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val game = RPSRound(round[0], round[1])
                    games.add(game)
                    line = fileReader.readLine()
                }
                println("Total score: " + games.stream().map { obj: RPSRound -> obj.points }
                    .reduce { a: Int, b: Int -> Integer.sum(a, b) }.orElse(-1))
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}