import java.io.File

fun main(args: Array<String>) {
    val hands = mutableListOf<Hand>()
    File(args[0]).forEachLine { line ->
        val parsed = line.split(' ')
        hands.add(Hand(parsed[0], parsed[1].toInt()))
    }
    hands.sort()
    //println("Hands: ${hands.map { it.cards }}")
    val winnings = hands.mapIndexed { idx, it -> it.bid * (idx+1) }//.sum()
    //println("Winnings array: $winnings")
    println("Winnings ${winnings.sum()}")
}

enum class Result {
    HighCard,
    OnePair,
    TwoPair,
    ThreeK,
    FullH,
    FourK,
    FiveK
}

data class Hand(val cards: String, val bid: Int): Comparable<Hand> {
    private fun getResult(): Result {
        val freq = IntArray(13) { 0 }
        cards.forEach { freq[it.toCard()]++ }
        val max = freq.drop(1).max()
        val idx = freq.drop(1).indexOf(max)
        return when(max+freq[0]) {
            5 -> Result.FiveK
            4 -> Result.FourK
            3 -> {
                if (freq.drop(1).filterIndexed{ id, _ -> id != idx }.any { it == 2 })  Result.FullH else Result.ThreeK
            }
            2 -> {
                if (freq.drop(1).filterIndexed{ id, _ -> id != idx }.count { it == 2 } > 0) Result.TwoPair else Result.OnePair
            }
            else -> Result.HighCard
        }
    }

    override fun compareTo(other: Hand): Int {
        val thisRes = this.getResult()
        val otherRes = other.getResult()
        if(thisRes > otherRes) return 1
        if(thisRes < otherRes) return -1
        cards.indices.forEach{
            if(this.cards[it].toCard() > other.cards[it].toCard()) return 1
            if(this.cards[it].toCard() < other.cards[it].toCard()) return -1
        }
        return 0
    }

    private fun Char.toCard(): Int {
        return when(this) {
            '2' -> 1
            '3' -> 2
            '4' -> 3
            '5' -> 4
            '6' -> 5
            '7' -> 6
            '8' -> 7
            '9' -> 8
            'T' -> 9
            'J' -> 0
            'Q' -> 10
            'K' -> 11
            'A' -> 12
            else -> -1
        }
    }
}