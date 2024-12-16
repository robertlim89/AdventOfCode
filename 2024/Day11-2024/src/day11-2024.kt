fun main(args: Array<String>) {
    Day11(args[0]).run()
}

class Day11(filename: String): BaseDay(filename) {
    private var list = listOf<Long>()

    private var memo = mutableMapOf(0L to listOf(1L))

    private fun process() {
        list = lines[0].splitToLongs(" ")
    }

    override fun part1(): String {
        process()
        repeat(25) {
            val newList = mutableListOf<Long>()
            list.forEach{
                if (memo.containsKey(it)) {
                    newList.addAll(memo[it]!!)
                } else if (it.toString().length % 2 == 0) {
                    val str = it.toString()
                    val mid = (str.length/2) - 1
                    memo[it] = listOf(str.substring(0..mid).toLong(), str.substring(mid+1).toLong())
                    newList.addAll(memo[it]!!)
                } else {
                    memo[it] = listOf(it*2024)
                    newList.addAll(memo[it]!!)
                }
            }
            list = newList
        }
        return list.size.toString()
    }

    override fun part2(): String {
        process()
        repeat(75) {
            val newList = mutableListOf<Long>()
            list.forEach{
                if (memo.containsKey(it)) {
                    newList.addAll(memo[it]!!)
                } else if (it.toString().length % 2 == 0) {
                    val str = it.toString()
                    val mid = (str.length/2) - 1
                    memo[it] = listOf(str.substring(0..mid).toLong(), str.substring(mid+1).toLong())
                    newList.addAll(memo[it]!!)
                } else {
                    memo[it] = listOf(it*2024)
                    newList.addAll(memo[it]!!)
                }
            }
            list = newList
        }
        return list.size.toString()
    }
}