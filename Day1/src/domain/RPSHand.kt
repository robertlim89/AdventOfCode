package domain

enum class RPSHand(val points: Int) {
    ROCK(1), PAPER(2), SCISSORS(3);

    fun getPointsAgainst(other: RPSHand): Int {
        return if (this == other) 3 else when (this) {
            ROCK -> if (other == PAPER) 0 else 6
            PAPER -> if (other == SCISSORS) 0 else 6
            SCISSORS -> if (other == ROCK) 0 else 6
        }
    }

    fun getHandFor(result: String?): RPSHand {
        return when (result) {
            "X" -> loser(this)
            "Y" -> this
            "Z" -> winner(this)
            else -> throw IllegalArgumentException()
        }
    }

    companion object {
        fun getMatching(variable: String?): RPSHand {
            return when (variable) {
                "A", "X" -> ROCK
                "B", "Y" -> PAPER
                "C", "Z" -> SCISSORS
                else -> throw IllegalArgumentException()
            }
        }

        private fun winner(hand: RPSHand): RPSHand = when (hand) {
            ROCK -> PAPER
            PAPER -> SCISSORS
            SCISSORS -> ROCK
        }

        private fun loser(hand: RPSHand): RPSHand = when (hand) {
            ROCK -> SCISSORS
            PAPER -> ROCK
            SCISSORS -> PAPER
        }
    }
}