import domain.Directory
import domain.DirectoryFile
import java.io.File

fun main(args: Array<String>) {
    val root = Directory("/", null)
    var current = root
    File(args[0]).forEachLine { line ->
        if (line.startsWith("$ cd")) {
            val directoryName = line.takeLast(line.length - 5)
            when (directoryName) {
                "/" -> current = root
                "src/main" -> current = current.parent!!
                else -> {
                    current = current.getChild(directoryName)
                }
            }
        } else if (line.startsWith("$ ls")) {
            return@forEachLine
        } else if (line.startsWith("dir")) {
            val directoryName = line.substringAfter(' ')
            current.addChild(Directory(directoryName, current))
        } else {
            val stats = line.split(' ')
            current.addChild(DirectoryFile(stats[1], stats[0].toLong()))
        }
    }
    val directoryList = getAllDirectories(root)

    println("Directories below 100k: ${directoryList.filter { d -> d.second <= 100000 }.map{it.second}}")
    println("Total size: ${directoryList.filter { d -> d.second <= 100000 }.sumOf{p -> p.second}}")
    val required = root.getSize() - 70000000 + 30000000
    println("We need $required disk space")
    val firstMatch = directoryList.sortedBy { it.second }.first { p -> p.second > required }
    println("firstMatch ${firstMatch}")
}

fun getAllDirectories(root: Directory): List<Pair<String, Long>> {
    val files = root.getFiles().filterIsInstance<Directory>()
    if (files.isEmpty()) return listOf(Pair(root.name, root.getSize()))
    return files.map { d -> getAllDirectories(d) }.reduce { acc, directories -> acc + directories } + listOf(Pair(root.name, root.getSize()))
}