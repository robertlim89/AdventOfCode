import domain.Elf;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CalorieCounter {
    public static void main(String[] args) {
        var elves = new ArrayList<Elf>();
        try (var fileReader = new BufferedReader(new FileReader(args[0]))) {
            var line = fileReader.readLine();
            var elf = new Elf();
            while (line != null) {
                if (line.isEmpty()) {
                    elves.add(elf);
                    elf = new Elf();
                } else {
                    elf.addFood(Integer.parseInt(line));
                }
                line = fileReader.readLine();
            }
            if (elf.hasFood())
                elves.add(elf);
            elves.sort(Collections.reverseOrder(Comparator.comparingInt(Elf::getCalories)));
            var total = elves.get(0).getCalories() + elves.get(1).getCalories() + elves.get(2).getCalories();

            System.out.println("Max calorie count of elf is: " + total);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
