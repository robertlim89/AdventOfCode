package domain

class Elf {
    private val foodCalories = mutableListOf<Int>()
    fun addFood(calories: Int) {
        foodCalories.add(calories)
    }

    fun hasFood(): Boolean = foodCalories.isNotEmpty()

    fun calories(): Int = foodCalories.stream().reduce { a: Int, b: Int -> Integer.sum(a, b) }.orElse(0)
}