package domain;

public class RPSRound {
    private final RPSHand opponent;
    private final RPSHand player;

    public RPSRound(String opponent, String result) {
        this.opponent = RPSHand.getMatching(opponent);
        this.player = this.opponent.getHandFor(result);
    }

    public int getPoints() {
        return player.getPoints() + player.getPointsAgainst(opponent);
    }
}
