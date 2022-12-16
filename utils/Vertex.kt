data class Vertex<T>(val index: String, val data: T)

data class Edge<T>(val source: Vertex<T>, val dest: Vertex<T>, val weight: Double? = null)

class Graph<T> {
    private val adjacencyMap: HashMap<T, HashSet<T>> = HashMap()
    private val list = mutableListOf<T>()

    fun addEdge(source: T, dest: T) {
        adjacencyMap.computeIfAbsent(source) { HashSet() }
            .add(dest)

        adjacencyMap.computeIfAbsent(dest) { HashSet() }
            .add(source)
    }

    fun getAllVertexes() = list

}

