package domain

class RPSRound(opponent: String?, result: String?) {
    private val opponent: RPSHand
    private val player: RPSHand

    init {
        this.opponent = RPSHand.getMatching(opponent)
        player = this.opponent.getHandFor(result)
    }

    val points: Int
        get() = player.points + player.getPointsAgainst(opponent)
}