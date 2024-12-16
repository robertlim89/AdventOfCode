fun String.splitToLongs(separator: String): List<Long> {
    return this.split(separator).map { it.toLong() }
}