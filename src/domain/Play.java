package domain;

public enum Play {
    ROCK("A", "X", 1),
    PAPER("B", "Y", 2),
    SCISSORS("C", "Z", 3);

    private final String asOpponent;
    private final String asPlayer;

    private final int points;

    Play(String asOpponent, String asPlayer, int points) {
        this.asOpponent = asOpponent;
        this.asPlayer = asPlayer;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public static Play getMatching(String variable){
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

    public int getWinner(Play other) {
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

    private Play getWinner() {
        switch(this) {
            case ROCK: return PAPER;
            case PAPER: return SCISSORS;
            case SCISSORS: return ROCK;
            default:
                throw new IllegalArgumentException();
        }
    }

    private Play getLoser() {
        switch(this) {
            case ROCK: return SCISSORS;
            case PAPER: return ROCK;
            case SCISSORS: return PAPER;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Play getResultFor(String player) {
        switch(player) {
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
