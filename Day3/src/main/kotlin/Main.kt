import java.io.File

fun main(args: Array<String>) {
    val priorities = mutableListOf<Int>()
    val group = mutableListOf<List<Char>>()
    File(args[0]).forEachLine { line ->
        val rucksack = line.toCharArray().sorted().distinct()
        group.add(rucksack)
        if(group.size == 3) {
            for(item in group[0]) {
                if(group[1].contains(item) && group[2].contains(item)) {
                    priorities.add(item.toPriority())
                }
            }
            group.clear()
        }
    }
    println(priorities.sum())
}

fun Char.toPriority(): Int {
    return if (code >= 97) code - 96 else code - 64 + 26
}