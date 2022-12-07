package domain

class Directory(name: String, val parent: Directory?) : Item(name) {

    private val files = mutableMapOf<String, Item>()
    override fun getSize(): Long = files.values.sumOf(Item::getSize)

    fun addChild(newItem: Item) {
        files.put(newItem.name, newItem)
    }

    fun getChild(name: String) = files.getValue(name) as Directory

    override fun toString(): String {
        return name + files.values.toString()
    }

    fun getFiles(): Collection<Item> = files.values
}