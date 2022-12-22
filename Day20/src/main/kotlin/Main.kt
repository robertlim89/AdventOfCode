import java.io.File

fun main(args: Array<String>) {
    println("Solution = ${solve(args[0], args[1].toLong(), args.drop(2).map { it.toInt() })}")
}

fun solve(filename: String, key: Long, indexes: List<Int>): Long {
    val list = File(filename).readLines().mapIndexed { index, it -> index to it.toLong() * key }.toMutableList()
    val fullSize = list.size
    //println("Initial arrangement:\n${list}\n")

    (1..10).forEach {
        list.indices.forEach { i ->
            val (index, value) = list.withIndex().first { e -> e.value.first == i }
            list.removeAt(index)
            val newIndex = (index.toLong() + value.second).mod(list.size)
            //println("newIndex $newIndex")
            list.add(newIndex, value)
        }
        //println("After $it rounds of mixing:\n${list.map{e -> e.second}}\n")
    }

    val zeroIndex = list.indexOfFirst{ it.second == 0L}
    return indexes.sumOf {
        println("#$it = ${list[((zeroIndex + it) % list.size)].second}")
        list[((zeroIndex + it) % list.size)].second
    }
}