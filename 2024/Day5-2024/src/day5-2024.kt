fun main(args: Array<String>) {
    Day5(args[0]).run()
}

class Day5(filename: String): BaseDay(filename) {
    private val rules = mutableListOf<Rule>()
    private val manuals = mutableListOf<List<Int>>()

    fun process() {
        var isRules = true
        lines.forEach { line ->
            if (line.isEmpty()) {
                isRules = false
            } else {
                if (isRules) {
                    val ruleItems = line.split('|')
                    rules.add(Rule(ruleItems[0].toInt(), ruleItems[1].toInt()))
                } else {
                    manuals.add(line.split(',').map { it.toInt() })
                }
            }
        }
    }

    override fun part1(): String {
        process()

        return manuals
            .filter { manual -> rules.all { rule -> rule.isValid(manual) } }
            .sumOf { manual -> manual[manual.size/2] }
            .toString() // 5166
    }

    override fun part2(): String {
        process()

        return manuals
            .filter { manual -> rules.any { rule -> !rule.isValid(manual) } }
            .associateWith { manual -> rules.filter { it.isApplicable(manual) }.toSet() }
            .map { (manual, applicableRules) ->
                val ordering = applicableRules.createOrdering().toMutableList()
                val editable = manual.toMutableList()
                editable.removeAll(ordering)
                ordering.addAll(editable)
                ordering
            }
            .sumOf { manual -> manual[manual.size/2] }
            .toString() // 4679
    }

    private fun Set<Rule>.createOrdering(): List<Int> {
        var beforeMap = mutableMapOf<Int, MutableSet<Int>>()
        this.forEach{ rule ->
            val existing = beforeMap.getOrDefault(rule.after, mutableSetOf())
            existing.add(rule.before)
            beforeMap.put(rule.after, existing)
            beforeMap.put(rule.before, beforeMap.getOrDefault(rule.before, mutableSetOf()))
        }
        val result = mutableListOf<Int>()
        while(beforeMap.isNotEmpty()) {
            val lastItem = beforeMap.filter { (_, value) -> value.isEmpty() }.keys.first()
            result.addLast(lastItem)
            beforeMap.remove(lastItem)
            beforeMap = beforeMap.onEach { (_, value) -> value.remove(lastItem) }
        }
        return result
    }

    data class Rule(val before: Int, val after: Int) {
        fun isValid(input: List<Int>): Boolean {
            val afterIndex = input.indexOf(after)
            val beforeIndex = input.indexOf(before)
            if (afterIndex == -1 || beforeIndex == -1) return true
            //println("[$beforeIndex] = $before and [$afterIndex] = $after")
            return beforeIndex < afterIndex
        }

        fun isApplicable(input: List<Int>) = input.containsAll(listOf(before, after))
    }
}