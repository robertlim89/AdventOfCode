import java.io.File

fun main(args: Array<String>) {
    var state = State.Seeds
    var seeds: Collection<LongRange> = listOf()
    val mappings = Array<MutableList<Mapping>>(7) { mutableListOf() }
    val cap = Array(7) { 0L }
    File(args[0]).forEachLine { line ->
        if(line.isNotEmpty()) {
            when(state) {
                State.Seeds -> {
                    seeds = part2(line.split(": ")[1].split(' ').filter { it.isNotEmpty() }.map { it.toLong() })
                }
                else -> {
                    if(!line.endsWith("map:")) {
                        val map = line.split(" ").map { it.toLong() }
                        val endRange = map[1]+map[2]-1
                        if(endRange > cap[state.ordinal-1]) cap[state.ordinal-1] = endRange
                        mappings[state.ordinal-1].add(Mapping(map[0], LongRange(map[1], endRange)))
                    }
                }
            }
        } else {
            println("Bumping state ${state} to ${state.bump()}")
            if(state != State.Seeds) println("Current: ${mappings[state.ordinal-1]}")
            state = state.bump()
        }
    }
    //println("Seeds: $seeds")
    val locations = seeds.map{ seedRange ->
        val smallest = seedRange.minOf{seed ->
            var value = seed
            mappings.forEachIndexed { idx, mapping ->
                if(value > cap[idx]) return@minOf Long.MAX_VALUE
                val mapped = mapping.filter { it.doesApply(value) }.firstOrNull()
                val newValue = mapped?.applyMap(value) ?: value
                //println("Mapping from ${State.values()[idx]} to ${State.values()[idx+1]} going from $value to ${newValue}")
                value = newValue
            }
            value
        }
        println("Seed range $seedRange smallest $smallest")
        smallest
    }
    println("Min location: ${locations.min()}")
}

fun part1(input: List<Long>): Collection<LongRange> = input.map { LongRange(it, it) }

fun part2(input: List<Long>): Collection<LongRange> {
    val result = mutableSetOf<LongRange>()
    for(i in 0..input.lastIndex step 2) {
        result.add(LongRange(input[i], input[i]+input[i+1]-1))
    }
    return result
}

enum class State{
    Seeds,
    SeedToSoil,
    SoilToFertilizer,
    FertilizerToWater,
    WaterToLight,
    LightToTemp,
    TempToHumidity,
    HumidityToLocation;

    fun bump(): State = State.values()[(this.ordinal+1) % State.values().size]
}

data class Mapping(val toStart: Long, val range: LongRange) {
    fun doesApply(from: Long) = range.contains(from)
    fun applyMap(from: Long): Long? = if(range.contains(from)) toStart + from - range.first else null
}