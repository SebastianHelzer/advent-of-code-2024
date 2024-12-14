fun main() {
    fun blinkLots(input: String, count: Int = 25): Long {
        var stoneMap = buildMap<Long, Long> {
            parseInput(input).forEach {
                compute(it) { k, v -> v?.inc() ?: 1 }
            }
        }
        
        repeat(count) {
            stoneMap = buildMap {
                stoneMap.forEach { stone, count ->
                    blink(stone) {
                        compute(it) { k, v -> v?.plus(count) ?: count }
                    }
                }
            }
        }
        
        return stoneMap.values.sum()
    }
    
    check(blinkList(parseInput("0 1 10 99 999")).joinToString(" ") == "1 2024 1 0 9 9 2021976")
    
    """
        125 17
        253000 1 7
        253 0 2024 14168
        512072 1 20 24 28676032
        512 72 2024 2 0 2 4 2867 6032
        1036288 7 2 20 24 4048 1 4048 8096 28 67 60 32
        2097446912 14168 4048 2 0 2 4 40 48 2024 40 48 80 96 2 8 6 7 6 0 3 2
    """.trimIndent().lines().fold(emptyList<Long>()) { a, b ->
        if(a.isNotEmpty()) check(blinkList(a).joinToString(" ") == b)
        parseInput(b)
    }
    
    check(blinkLots("125 17", 6) == 22L)
    check(blinkLots("125 17") == 55312L)
    blinkLots("20 82084 1650 3 346355 363 7975858 0").apply { println() }
    blinkLots("20 82084 1650 3 346355 363 7975858 0", 75).apply {
        println()
        println(format())
    }
}

private inline fun blink(stone: Long, onNewStone: (Long) -> Unit) {
    val stoneString = stone.toString()
    when {
        stone == 0L -> onNewStone(1)
        stoneString.length % 2 == 0 -> {
            onNewStone(stoneString.take(stoneString.length / 2).toLong())
            onNewStone(stoneString.takeLast(stoneString.length / 2).dropWhile { it == '0' }.takeIf { it.isNotEmpty() }?.toLong() ?: 0)
        }
        else -> {
            val newValue = stone * 2024
            assert(newValue > 0)
            onNewStone(newValue)
        }
    }
}

private fun blinkList(stones: List<Long>): List<Long> = buildList {
    stones.forEach { stone ->
        blink(stone) { add(it) }
    }
}

private fun parseInput(input: String): List<Long> {
    return input.split(" ").map { it.toLong() }
}

private fun Long.format() = toString().reversed().windowed(3, 3, partialWindows = true).reversed().joinToString(",") { it.reversed() }