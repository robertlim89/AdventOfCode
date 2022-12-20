import kotlin.math.abs

data class Coord3D(val x: Int, val y: Int, val z: Int) {
    fun isNeighbour(other: Coord3D): Boolean {
        val dists = listOf(
            abs(x - other.x),
            abs(y - other.y),
            abs(z - other.z)
        )
        return dists.count { it == 1 } == 1 && dists.count { it == 0} == 2
    }

    fun isFacing(other: Coord3D): Boolean {
        val dists = listOf(
            abs(x - other.x),
            abs(y - other.y),
            abs(z - other.z)
        )
        return dists.count { it == 0} == 2
    }
}
