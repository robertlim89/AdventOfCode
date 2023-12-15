import java.io.File

fun main(args: Array<String>) {
    val boxes = Array<MutableList<Instruction>>(256) { mutableListOf() }
    val lines = File(args[0]).readLines()

    lines[0].split(',')
        .filter { it.isNotEmpty() }
        .map { lens ->
            val match = Regex("([a-z]+)([=|-])(\\d*)").matchEntire(lens)
            Instruction(
                match?.groups?.get(1)?.value ?: "",
                match?.groups?.get(2)?.value?.get(0) ?: '-',
                match?.groups?.get(3)?.value?.toIntOrNull()
            )
        }.forEach { instruction ->
            when (instruction.operation) {
                '=' -> {
                    val index = boxes[instruction.hash()].indexOfFirst { it.label == instruction.label }
                    if (index != -1) {
                        boxes[instruction.hash()][index].focalLength = instruction.focalLength
                    } else {
                        boxes[instruction.hash()].add(instruction)
                    }
                }

                '-' -> {
                    boxes[instruction.hash()].removeIf { it.label == instruction.label }
                }
            }
        }
    println("Boxes: ${boxes.mapIndexed { ind, it -> ind to it }.filter { it.second.isNotEmpty() }}")

    var total = 0
    boxes.forEachIndexed { boxId, instructions ->
        if(instructions.isNotEmpty()) {
            instructions.forEachIndexed { id, instruction ->
                val sum = (boxId + 1) * (id + 1) * instruction.focalLength!!
                println("Focusing power of $instruction is $sum")
                total += sum
            }
        }
    }
    println(total)
    //val hashes = lenses.map{ it.hash() }
    //println("sum ${hashes.sum()}")
}

data class Instruction(val label: String, val operation: Char, var focalLength: Int? = null) {
    fun hash(): Int {
        var total = 0
        label.forEach {
            total += it.code
            total = total * 17 % 256
        }
        return total
    }
}

fun String.hash(): Long {
    var total = 0L
    this.forEach {
        total += it.code
        total = total * 17 % 256
    }
    return total
}