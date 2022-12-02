package domain;

import java.util.ArrayList;
import java.util.List;

public class Elf {
    private final List<Integer> foodCalories = new ArrayList<>();

    public void addFood(int calories) {
        foodCalories.add(calories);
    }

    public boolean hasFood() {
        return !foodCalories.isEmpty();
    }

    public int getCalories() {
        return foodCalories.stream().reduce(Integer::sum).orElse(0);
    }
}
