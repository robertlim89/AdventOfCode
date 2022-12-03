import domain.Elf;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Day1 {
    public static void main(String[] args) {
        var elves = new ArrayList<Elf>();
        try (var fileReader = new BufferedReader(new FileReader(args[0]))) {
            var elf = new Elf();
            for(var line = fileReader.readLine(); line != null; line = fileReader.readLine()) {
                if (line.isEmpty() && elf.hasFood()) {
                    elves.add(elf);
                    elf = new Elf();
                } else {
                    elf.addFood(Integer.parseInt(line));
                }
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
