fun main(args: Array<String>) {
    Day9(args[0]).run()
}

class Day9(filename: String): BaseDay(filename) {
    private var blocks = listOf<String>()
    private var blocks2 = listOf<Block>()
    private var legit = 0

    private fun process() {
        var id = 0
        var inBlock = true
        blocks = lines[0].flatMap { c ->
            val count = c.digitToInt()
            val blockId = if (inBlock) id++.toString() else "."
            legit += if (inBlock) count else 0
            inBlock = !inBlock
            Array(count) {blockId}.toList()
        }
    }

    private fun process2() {
        var id = 0
        var inBlock = true
        blocks2 = lines[0].map { c ->
            val count = c.digitToInt()
            val blockId = if (inBlock) id++ else -1
            legit += if (inBlock) count else 0
            var block = Block(blockId, count)
            inBlock = !inBlock
            block
        }
    }

    override fun part1(): String {
        process()
        var start = 0
        var end = blocks.size-1
        val res = Array(legit) {""}
        while(start <= end) {
            if (blocks[start] == ".") {
                while(blocks[end] == ".") end--
                res[start] = blocks[end--]
            } else {
                res[start] = blocks[start]
            }
            start++
        }
        return res.indices.sumOf{ id -> id * res[id].toLong() }.toString()
    }

    override fun part2(): String {
        process2()
        var changed = true
        var res = blocks2.toMutableList()
        while(changed) {
            changed = false
            val res2 = res.toMutableList()
            res.forEachIndexed { id, item ->
                if (item.id == -1) {
                    var gap = item.size
                    var lastIndex = res2.indexOfLast { b -> b.size <= gap && b.id != -1 }
                    while(lastIndex > id && gap > 0) {
                        changed = true
                        val found = res2[lastIndex]
                        gap -= found.size
                        res2[id].id = found.id
                        res2[id].size = found.size
                        if (gap > 0) {
                            res2.add(id+1, Block(-1, gap))
                        }
                        lastIndex = res2.indexOfLast { b -> b.size <= gap && b.id != -1 }
                    }
                }
                res2.add(item)
            }
            res = res2
        }
        return ""
    }
}

data class Block(var id: Int, var size: Int) {
    override fun toString(): String {
        return Array(size) { id }.toList().joinToString("")
    }
}