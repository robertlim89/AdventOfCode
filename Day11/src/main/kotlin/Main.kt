import java.io.File

fun main(args: Array<String>) {
    val monkeys = mutableListOf<Monkey>()
    var monkey =
        Monkey(ArrayDeque(), { i: Long -> i }, { i: Long -> true }, { v: Long -> }, { i: Long -> }, { i: Long -> i })
    var maxWorry = 1L
    File(args[0]).forEachLine { line ->
        if (line.startsWith("Monkey")) {
            // do nothing
        } else if (line.contains("Starting items")) {
            monkey.items.addAll(line.substring(18, line.length).split(", ").map(String::toLong))
        } else if (line.contains("Operation")) {
            val multiplier = line.substring(25, line.length).toLongOrNull()
            when (line.substring(23, 24)) {
                "*" -> monkey.operation = { i: Long -> i * (multiplier ?: i) }
                "+" -> monkey.operation = { i: Long -> i + (multiplier ?: i) }
            }
        } else if (line.contains("Test")) {
            val quotient = line.substring(21, line.length).toLong()
            maxWorry *= quotient
            monkey.test = { i: Long -> i % quotient == 0L }
        } else if (line.contains("If true")) {
            val monkeyIndex = line.substring(29, line.length).toInt()
            monkey.ifTrue = { v: Long -> monkeys[monkeyIndex].items.addLast(v) }
        } else if (line.contains("If false")) {
            val monkeyIndex = line.substring(30, line.length).toInt()
            monkey.ifFalse = { v: Long -> monkeys[monkeyIndex].items.addLast(v) }
        } else {
            monkeys.add(monkey)
            monkey = Monkey(
                ArrayDeque(),
                { i: Long -> i },
                { i: Long -> true },
                { v: Long -> },
                { i: Long -> },
                { i: Long -> i })
        }
    }
    monkeys.add(monkey)
    if (args[1] == "part1") {
        monkeys.forEach { it.worryManager = { i: Long -> i / 3 } }
    } else if (args[1] == "part2") {
        monkeys.forEach { it.worryManager = { i: Long -> i % maxWorry } }
    }

    repeat((1..10000).count()) {
        monkeys.forEach { it.inspectItems() }
    }
    monkeys.forEachIndexed { index, it ->
        println("Monkey $index inspected ${it.business} times")
    }
    val business = monkeys.map { it.business }.sortedByDescending{ it }.subList(0, 2).reduce { acc, l ->
        acc*l }
    println("Monkey business = $business")
}

data class Monkey(
    var items: ArrayDeque<Long>,
    var operation: (v: Long) -> Long,
    var test: (v: Long) -> Boolean,
    var ifTrue: (v: Long) -> Unit,
    var ifFalse: (v: Long) -> Unit,
    var worryManager: (v: Long) -> Long,
    var business: Long = 0
) {
    fun inspectItems() {
        while (items.isNotEmpty()) {
            var item = items.removeFirst()
            business++
            item = operation.invoke(item)
            item = worryManager.invoke(item)
            if (test.invoke(item)) ifTrue.invoke(item) else ifFalse.invoke(item)
        }
    }
}