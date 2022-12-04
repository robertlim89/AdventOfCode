import domain.Elf
import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val elves = ArrayList<Elf>()
    var elf = Elf()
    File(args[0]).forEachLine { line ->
        if (line.isEmpty() && elf.hasFood()) {
            elves.add(elf)
            elf = Elf()
        } else {
            elf.addFood(line.toInt())
        }
    }
    if (elf.hasFood()) elves.add(elf)
    elves.sortWith(Collections.reverseOrder(Comparator.comparingInt { it.calories() }))
    val total = elves[0].calories() + elves[1].calories() + elves[2].calories()
    println("Max calorie count of elf is: $total")
}