fun main(args: Array<String>) {
    Day7(args[0]).run()
}

class Day7(filename: String): BaseDay(filename) {
    override fun part1(): String {
        return solve(listOf(Operator.TIMES, Operator.PLUS)).toString()
    }

    override fun part2(): String {
        return solve(Operator.values().toList()).toString()
    }

    private val calibrations = lines.map {
        val (answer, options) = it.split(": ", limit = 2)
        answer.toLong() to options.splitToLongs(" ")
    }

    private fun solve(ops: List<Operator>) = calibrations
        .filter { (answer, options) ->
            buildList { options.findSolutions(answer, 1, listOf(), ops, this) }
                .isNotEmpty()
        }
        .sumOf { (answer) -> answer }

    private fun List<Long>.findSolutions(
        answer: Long,
        idx: Int,
        operations: List<Operation>,
        operators: List<Operator>,
        equations: MutableList<List<Operation>>,
    ): Unit = operators.forEach {
        val equation = operations + Operation(it, this[idx])
        if (idx == this.lastIndex) {
            if (equation.fold(this[0]) { num, op -> op(num) } == answer)
                equations += equation
        } else findSolutions(answer, idx + 1, equation, operators, equations)
    }

    private class Operation(private val operator: Operator, private val value: Long) {
        operator fun invoke(num: Long) = operator(num, value)
    }

    private enum class Operator(val calculate: (Long, Long) -> Long) {
        TIMES(Long::times),
        PLUS(Long::plus),
        CONCAT({ left, right -> "$left$right".toLong() });

        operator fun invoke(left: Long, right: Long) = calculate(left, right)
    }

}