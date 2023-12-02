package domain

class DirectoryFile (name: String, private val size: Long): Item(name) {
    override fun getSize(): Long = size

    override fun toString(): String {
        return "$name ($size)"
    }
}