package domain;

public class RPSRound {
    private final Play opponent;
    private final Play player;

    public RPSRound(String opponent, String player) {
        this.opponent = Play.getMatching(opponent);
        this.player = this.opponent.getResultFor(player);
    }

    public int getPoints() {
        return player.getPoints() + player.getWinner(opponent);
    }
}
