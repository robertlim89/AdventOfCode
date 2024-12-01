import com.sun.source.tree.Tree

fun main(args: Array<String>) {
    Day19(args[0]).run()
}

class Day19(fileName: String) : BaseDay(fileName) {

    var workflows = mapOf<String, List<Workflow>>()
    override fun part1(): String {
        val indexOf = lines.indexOf("")
        workflows = lines.subList(0, indexOf).map{ line ->
            val name = line.split('{')
            val wf = name[1].split(',', '}').filter{ it.isNotEmpty()}
            val list = wf.dropLast(1).mapIndexed{ ind, w ->
                val m = w.drop(2).split(':')
                if(ind != wf.size - 2) {
                    Workflow(w[0], w[1], m[0].toInt(), m[1])
                } else {
                    Workflow(w[0], w[1], m[0].toInt(), m[1], wf.last())
                }
            }
            name[0] to list
        }.toMap()

        val ratings = lines.subList(indexOf+1, lines.size).map{line ->
            val parsed = Regex("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)}").matchEntire(line)
            parsed?.groupValues?.let { Rating(it[1].toInt(), it[2].toInt(), it[3].toInt(), it[4].toInt()) } ?: throw IllegalArgumentException()
        }

        val queue = ArrayDeque(ratings)
        return ratings.sumOf { rating ->
            var result = workflows["in"]!!.resolve(rating)
            while(result !in listOf("R", "A")) {
                result = workflows[result]!!.resolve(rating)
            }
            if(result == "A") rating.sum() else 0
        }.toString()
    }

    override fun part2(): String {
        val list: TreeNode? = TreeNode(workflows["in"]!!.first().predicate)
        var current: TreeNode? = null
        val queue = ArrayDeque<Workflow>()
        queue.addAll(workflows["in"]!!.drop(1))
        var final = ""
        while(queue.isNotEmpty()) {
            val workflow = queue.removeFirst()
            workflow.
        }
    }
    data class TreeNode(val predicate: ((Rating) -> Boolean)? = null,
                        val value: String? = null,
                        var left: TreeNode? = null,
                        var right: TreeNode? = null)
    data class Rating(val x: Int, val m: Int, val a: Int, val s: Int) {
        fun sum() = x+m+a+s
    }

    fun List<Workflow>.resolve(rating: Rating): String {
        return this.firstNotNullOfOrNull { it.predicate.invoke(rating) } ?: this.last().unmatch!!
    }

    data class Workflow(val arg: Char, val operation: Char, val operand: Int,
                        val match: String, val unmatch: String? = null)
        {
        val predicate: (Rating) -> String? = {
            val value = when (arg) {
                'x' -> {
                    it.x
                }
                'm' -> {
                    it.m
                }
                'a' -> {
                    it.a
                }
                's' -> {
                    it.s
                }
                else -> throw IllegalArgumentException()
            }
            when (operation) {
                '>' ->  if(value > operand) match else unmatch
                '<' ->  if(value < operand) match else unmatch
                else -> throw IllegalArgumentException()
            }
        }

        val getFunction: (Rating) -> Boolean = {
            val value = when (arg) {
                'x' -> {
                    it.x
                }
                'm' -> {
                    it.m
                }
                'a' -> {
                    it.a
                }
                's' -> {
                    it.s
                }
                else -> throw IllegalArgumentException()
            }
            when (operation) {
                '>' ->  value > operand
                '<' ->  value < operand
                else -> throw IllegalArgumentException()
            }
        }
    }
}
