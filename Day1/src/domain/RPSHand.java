package domain;

public enum RPSHand {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    private final int points;

    RPSHand(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public static RPSHand getMatching(String variable) {
        switch(variable) {
            case "A":
            case "X":
                return ROCK;
            case "B":
            case "Y":
                return PAPER;
            case "C":
            case "Z":
                return SCISSORS;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getPointsAgainst(RPSHand other) {
        if (this == other) return 3;
        switch (this) {
            case ROCK:
                return other == PAPER ? 0 : 6;
            case PAPER:
                return other == SCISSORS ? 0 : 6;
            case SCISSORS:
                return other == ROCK ? 0 : 6;
            default:
                throw new IllegalArgumentException();
        }
    }

    private RPSHand getWinner() {
        switch(this) {
            case ROCK: return PAPER;
            case PAPER: return SCISSORS;
            case SCISSORS: return ROCK;
            default:
                throw new IllegalArgumentException();
        }
    }

    private RPSHand getLoser() {
        switch(this) {
            case ROCK: return SCISSORS;
            case PAPER: return ROCK;
            case SCISSORS: return PAPER;
            default:
                throw new IllegalArgumentException();
        }
    }

    public RPSHand getHandFor(String result) {
        switch(result) {
            case "X":
                return getLoser();
            case "Y":
                return this;
            case "Z":
                return getWinner();
            default:
                throw new IllegalArgumentException();
        }
    }
}
