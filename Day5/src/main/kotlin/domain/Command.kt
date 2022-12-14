package domain

import java.lang.IllegalArgumentException

data class Command(val count: Int, val sourceStack: Int, val destStack: Int)

fun String.parseCommand(): Command {
    val commands = "move (\\d+) from (\\d+) to (\\d+)".toRegex().find(this)?.groupValues
    return commands?.let { Command(it[1].toInt(), it[2].toInt(), it[3].toInt()) } ?: throw IllegalArgumentException(this)
}