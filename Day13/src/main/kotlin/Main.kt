import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

fun main(args: Array<String>) {
    val objectMapper = ObjectMapper()
    val packets = mutableListOf<List<*>>()
    File(args[0]).forEachLine { line ->
        if (line.isNotEmpty()) {
            packets.add(objectMapper.readValue(line, List::class.java))
        }
    }
    var sumIndex = 0
    (0 until packets.size step 2).forEach { i ->
        val realIndex =  (i / 2) + 1
        //println("Checking pair $realIndex")
        sumIndex += if (packets[i].isInRightOrder(packets[i + 1])) {
            //println("Pair $realIndex is good\n")
            realIndex
        } else 0
    }
    println("Sum of indexes: $sumIndex")
    packets.add(listOf(listOf(2)))
    packets.add(listOf(listOf(6)))
    packets.sortWith { o1, o2 -> -o1.isInRightOrderWithResult(o2) }
    val dividerkey1 = packets.indexOf(listOf(listOf(2)))+1
    val dividerkey2 = packets.indexOf(listOf(listOf(6)))+1
    //println(packets)
    println("key1 = $dividerkey1, key2 = $dividerkey2, result = ${dividerkey1*dividerkey2}")
}

fun List<*>.isInRightOrder(right: List<*>): Boolean {
    return this.isInRightOrderWithResult(right) == 1
}

fun List<*>.isInRightOrderWithResult(right: List<*>): Int {
    for((index, value) in this.withIndex()) {
        if(index >= right.size) return -1
        val second = right[index]
        if (value is Int && second is Int) {
            if (value != second) return if(value < second) 1 else -1
        }
        else if (value is List<*> && second is List<*>) {
            //println("Two lists")
            val ret = value.isInRightOrderWithResult(second)
            if(ret != 0) return ret
        }
        else if (second is Int) {
            //println("List and Int")
            val list = value as List<*>
            if(list.isEmpty()) return 1
            val ret = list.isInRightOrderWithResult(listOf(second))
            if(ret != 0) return ret
        }
        else {
            //println("Int and List")
            val list = second as List<*>
            if(list.isEmpty()) return -1
            val ret = listOf(value).isInRightOrderWithResult(second)
            if(ret != 0) return ret
        }
    }
    return if(this.size < right.size) 1 else 0
}
