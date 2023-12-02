import java.io.File

fun main(args: Array<String>) {
    solve(args[0], args[1].equals("1"))
}

fun solve(filename: String, part1Solve: Boolean) {
    val costs = mutableListOf<List<List<Int>>>()
    var numbers = """-?\d+""".toRegex()
    File(filename).forEachLine { line ->
        val nums = numbers.findAll(line).map { it.value.toInt() }.toList()
        costs.add(
            listOf(
                listOf(nums[1], 0, 0),
                listOf(nums[2], 0, 0),
                listOf(nums[3], nums[4], 0),
                listOf(nums[5], 0, nums[6]),
            )
        )
    }
    println(costs)
    costs.forEach { cost ->
        val robots = arrayOf(1, 0, 0, 0)
        var materials = mutableListOf(0, 0, 0, 0)
        (1..24).forEach {minute ->
            println("Minute $minute")
            val goalRobotIndex =
                if (robots.indexOfFirst { it == 0 } == -1) robots.lastIndex else robots.indexOfFirst { it == 0 }
            val costGoal = cost[goalRobotIndex]
            println("I want a ${goalRobotIndex.toRobotType()} robot costing ${costGoal}")

            // Can I get the robot
            val newRobotIndex = if(costGoal.canAfford(materials)) goalRobotIndex else {
                println("Can't afford it, only have $materials :(")
                val waitTime = costGoal.mapIndexed { index, cost ->
                    if(cost == 0) return@mapIndexed 0
                    else if(robots[index] == 0) return@mapIndexed Int.MAX_VALUE
                    return@mapIndexed (cost - materials[index])/robots[index]
                }
                val averageTime = waitTime.filter{ it != 0}.average()
                var subRobot = waitTime.indexOfFirst { it/averageTime > 1.5 }
                println(waitTime)
                if(subRobot != -1) {
                    println("Lets get ${subRobot.toRobotType()} instead since the wait time is ${waitTime[subRobot]} over $averageTime")
                    if(cost[subRobot].mapIndexed{ index, cost -> materials[index] >= cost}.all{it}) {
                        println("Gonna get a ${subRobot.toRobotType()}")
                    }
                    else {
                        println("Way too poor D:")
                        subRobot = -1
                    }
                }
                subRobot
            }

            // Do I have the right ratio of robots to get it


            materials = materials.mapIndexed { index, mat -> mat + robots[index] }.toMutableList()

            if (newRobotIndex != -1) {
                println("Adding robot ${newRobotIndex.toRobotType()}")
                cost[newRobotIndex].forEachIndexed { index, i -> materials[index] -= i }
                robots[newRobotIndex]++
            }
            println("List of materials $materials\n")
        }
        println(materials[3])
    }
}

fun List<Int>.canAfford(materials: List<Int>) : Boolean =
    this.mapIndexed{ index, cost -> materials[index] >= cost}.all{it}

fun Int.toRobotType(): String {
    return when (this) {
        0 -> "Ore"
        1 -> "Clay"
        2 -> "Obsidian"
        3 -> "Geode"
        else -> "?"
    }
}