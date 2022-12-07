package domain

abstract class Item(val name: String) {
    abstract fun getSize(): Long
}