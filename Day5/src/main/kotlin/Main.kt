import domain.parseCommand
import java.io.BufferedReader
import java.io.FileReader

fun main(args: Array<String>) {
    val stacks = mutableListOf<ArrayDeque<String>>()
    val reader = BufferedReader(FileReader(args[0]))
    var line = reader.readLine()

    // Read the stacks
    line.readStack().forEach {
        val stack = ArrayDeque<String>()
        it?.let { stack.addLast(it) }
        stacks.add(stack)
    }

    line = reader.readLine()
    var isReadingStacks = true
    while (isReadingStacks && line != null) {
        val level = line.readStack()
        if (level.all { it == null }) {
            isReadingStacks = false
        } else {
            for ((index, place) in level.withIndex()) {
                val stack = stacks[index]
                place?.let { stack.addLast(it) }
            }
        }
        line = reader.readLine()
    }

    line = reader.readLine()
    // Do the moving
    while (line != null) {
        val command = line.parseCommand()
        val movedItems = (1..command.count).map { stacks[command.sourceStack - 1].removeFirst() }
        stacks[command.destStack - 1].addAll(0, movedItems)
        line = reader.readLine()
    }

    println("Top of each stack has ${stacks.map { it.first() }}")
}

fun String.readStack(): List<String?> {
    val level = this.chunked(4)
    return level.map { """\[(\w)]""".toRegex().find(it)?.groupValues?.get(1) }
}

