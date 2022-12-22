import java.io.File
import java.lang.IllegalArgumentException

fun main(args: Array<String>) {
    solve(args[0], args[1].equals("1"))
}

fun solve(filename: String, solvePart1: Boolean) {
    val monkeys = File(filename).readLines().filter { solvePart1 || it.take(4) != "humn" }.associate { line ->
        val monkeyName = line.take(4)
        monkeyName to
                if (line.substring(6, line.length).toLongOrNull() != null) {
                    val value = line.substring(6, line.length).toLong()
                    Monkey(value, null, null, null)
                } else {
                    Monkey(null, line.substring(11, 12)[0], line.substring(6, 10), line.substring(13, 17))
                }
    }
    if (solvePart1) {
        println("Monkey['root'] calls ${solvePart1(monkeys["root"]!!, monkeys)}")
    } else {
        val root = monkeys["root"]!!
        val leftEqn = solveRPN(solvePart2(root.leftEqn!!, monkeys))
        val rightEqn = solveRPN(solvePart2(root.rightEqn!!, monkeys))
        val target = leftEqn ?: rightEqn!!
        val solution = solveEquation(leftEqn?.let { root.rightEqn } ?: root.leftEqn, monkeys, target)
        println("You should call $solution")
    }
}

data class Monkey(
    var value: Long?,
    val operation: Char? = null,
    val leftEqn: String? = null,
    val rightEqn: String? = null
)

fun solvePart1(monkey: Monkey, group: Map<String, Monkey>): Long {
    if (monkey.value == null) {
        monkey.value = when (monkey.operation) {
            '*' -> solvePart1(group[monkey.leftEqn!!]!!, group) * solvePart1(group[monkey.rightEqn!!]!!, group)
            '+' -> solvePart1(group[monkey.leftEqn!!]!!, group) + solvePart1(group[monkey.rightEqn!!]!!, group)
            '-' -> solvePart1(group[monkey.leftEqn!!]!!, group) - solvePart1(group[monkey.rightEqn!!]!!, group)
            '/' -> solvePart1(group[monkey.leftEqn!!]!!, group) / solvePart1(group[monkey.rightEqn!!]!!, group)
            else -> throw IllegalArgumentException("Unknown operator")
        }
    }
    return monkey.value!!
}

fun solvePart2(name: String, monkeys: Map<String, Monkey>): List<String> {
    if (name == "humn") return listOf("x")
    val monkey = monkeys[name]!!
    if (monkey.value != null) return listOf(monkey.value.toString())
    val result = mutableListOf<String>()
    result += solvePart2(monkey.leftEqn!!, monkeys)
    result += solvePart2(monkey.rightEqn!!, monkeys)
    result += monkey.operation!!.toString()
    return result
}

fun solveRPN(tokens: List<String>): Long? {
    println(tokens)
    val stack = ArrayDeque<Long>()
    for (token in tokens) {
        when (token) {
            "x" -> return null
            in listOf("+", "-", "*", "/") -> {
                val right = stack.removeFirst()
                val left = stack.removeFirst()
                val result = when (token) {
                    "+" -> left + right
                    "-" -> left - right
                    "*" -> left * right
                    "/" -> left / right
                    else -> throw IllegalArgumentException("Unknown operator")
                }
                stack.addFirst(result)
            }
            else -> stack.addFirst(token.toLong())
        }
    }
    return stack.removeFirst()
}

fun solveEquation(name: String, monkeys: Map<String, Monkey>, target: Long): Long {
    if (name == "humn") return target
    val root = monkeys[name]!!
    val left = solveRPN(solvePart2(root.leftEqn!!, monkeys))
    val right = solveRPN(solvePart2(root.rightEqn!!, monkeys))
    return when (root.operation) {
        '+' -> solveEquation(left?.let { root.rightEqn } ?: root.leftEqn, monkeys, target - (left ?: right)!!)
        '-' -> solveEquation(
            left?.let { root.rightEqn } ?: root.leftEqn,
            monkeys,
            left?.let { it - target } ?: (target + right!!))

        '*' -> solveEquation(left?.let { root.rightEqn } ?: root.leftEqn, monkeys, target / (left ?: right)!!)
        '/' -> solveEquation(
            left?.let { root.rightEqn } ?: root.leftEqn,
            monkeys,
            left?.let { it / target } ?: (target * right!!))

        else -> throw IllegalArgumentException("Unknown operator")
    }
}