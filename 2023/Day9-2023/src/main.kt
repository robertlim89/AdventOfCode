import java.io.File

fun main(args: Array<String>) {
    val sequences = mutableListOf<List<Int>>()
    File(args[0]).forEachLine {line ->
        sequences.add(line.split(' ').map{ it.toInt() }.toMutableList())
    }
    var total = 0L
    sequences.forEach { seq ->
        val successor = findSuccessor(seq)
        println("successor $successor")
        total += successor
    }
    println("Total $total")
}

fun findSuccessor(list: List<Int>): Int {
    if(list.all{ it == 0}) return 0
    val newSeq = (0 until list.lastIndex).map { list[it+1] - list[it] }
    return list.first - findSuccessor(newSeq)
}