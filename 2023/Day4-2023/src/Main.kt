import java.io.File

fun main(args: Array<String>) {
    var total = 0L
    var cards = 0L
    File(args[0]).forEachLine { cards++ }
    println("There are $cards")
    val copies = IntArray(cards.toInt()) { 1 }
    var index = 0
    File(args[0]).forEachLine { line ->
        val games = line.split(": ", " | ")
        val winningNumbers = games[1].split("\\s+".toRegex()).filter { it.isNotEmpty() }.map { it.toInt() }
        val drawnNumbers = games[2].split("\\s+".toRegex()).filter { it.isNotEmpty() }.map { it.toInt() }
        val matches = drawnNumbers.count { winningNumbers.contains(it) }
        cards += copies[index] * matches
        (0 until matches).forEach{ c -> copies[index+c+1] += copies[index] }
        val subtotal = if(matches == 0) 0 else copies[index] * Math.pow(2.0, matches - 1.0).toInt()
        println("Subtotal $subtotal $matches $cards")
        total += subtotal
        index++
    }
    println("Total $total Cards $cards")
}