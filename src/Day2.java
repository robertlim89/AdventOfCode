import domain.RPSRound;

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.IOException;
import java.util.ArrayList;

public class Day2 {
    public static void main(String[] args) {
        var games = new ArrayList<RPSRound>();
        try (var fileReader = new BufferedReader(new FileReader(args[0]))) {
            for(var line = fileReader.readLine(); line != null; line = fileReader.readLine()) {
                var round = line.split(" ");
                var game = new RPSRound(round[0], round[1]);
                games.add(game);
            }
            System.out.println("Total score: " + games.stream().map(RPSRound::getPoints).reduce(Integer::sum).orElse(-1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
