import java.math.BigInteger
import kotlin.time.measureTime

fun main() {
    fun expandToBlocks(diskMap: String): String {
        return buildString {
            diskMap.forEachIndexed { index, it ->
                if(Char(index / 2) > (Char.MAX_VALUE - 1)) throw IndexOutOfBoundsException()
                val digit = it.digitToInt()
                repeat(digit) {
                    if(index % 2 == 0) {
                        append(Char(index / 2))
                    } else {
                        append(Char.MAX_VALUE)
                    }
                }
            }
        }
    }

    fun Char.endDigitToInt(): Int {
        return this.minus(Char.MAX_VALUE.code).plus(11).plus('0'.code).digitToInt()
    }

    fun Int.intToEndDigit(): Char {
        val newDigit = Char.MAX_VALUE - 11 + this
        return newDigit
    }

    fun annotatedDiskMap(diskMap: String): String {
        return buildString {
            diskMap.forEachIndexed { index, it ->
                if(Char(index / 2) > (Char.MAX_VALUE - 11)) throw IndexOutOfBoundsException()
                val endDigit = it.digitToInt().intToEndDigit()
                append(endDigit)
                if(index % 2 == 0) {
                    append(Char(index / 2))
                } else {
                    append(Char.MAX_VALUE)
                }
            }
        }
    }

    fun parseInput(input: List<String>): String {
        val diskMap = input.first()
        return expandToBlocks(diskMap)
    }

    fun Collection<Char>.computeChecksum() = mapIndexed { index, c ->
        if(Char.MAX_VALUE == c) 0 else index * c.code.toLong()
    }.sumOf { it.toBigInteger() }



    fun expandAnnotatedDiskMap(it: List<Pair<Int, Char>>): String {
        return buildString {
            it.forEach { pair ->
                val digit = pair.first
                repeat(digit) {
                    append(pair.second)
                }
            }
        }
    }

    fun part1(input: List<String>): BigInteger {
        return parseInput(input).toMutableList().apply {
            while (true) {
                val left = indexOfFirst { it == Char.MAX_VALUE }
                val right = indexOfLast { it != Char.MAX_VALUE }
                if (left > right) break
                this[left] = this[right]
                this[right] = Char.MAX_VALUE
            }
        }.computeChecksum()
    }

    fun part2(input: List<String>): BigInteger {
        return annotatedDiskMap(input.first())
            .windowed(2, 2) { it.first().endDigitToInt() to it.last() }
            .toMutableList()
            .apply {
                reversed().filter { it.second != Char.MAX_VALUE }.forEach { pair ->
                    val spaceIndex = this.indexOfFirst { it.second == Char.MAX_VALUE && it.first >= pair.first }
                    val index = indexOf(pair)
                    if (spaceIndex != -1 && spaceIndex <= index) {
                        val space = this.removeAt(spaceIndex)
                        remove(pair)
                        add(spaceIndex, pair)
                        add(index, pair.copy(second = Char.MAX_VALUE))
                        if(space.first > pair.first) {
                            add(spaceIndex + 1, space.copy(first = space.first - pair.first))
                        }
                    }
                }
            }
            .let { expandAnnotatedDiskMap(it).toList().computeChecksum() }
    }

    (0..9).forEach {
        check(it.intToEndDigit().endDigitToInt() == it)
    }
    (Char.MAX_VALUE - 11..<Char.MAX_VALUE - 1).forEach {
        check(it.endDigitToInt().intToEndDigit() == it)
    }

    check(part1(listOf("12345")).apply { println() } == 60L.toBigInteger())

    val testInput = """
       2333133121414131402
    """.trimIndent().lines()
    check(part1(testInput).apply { println() } == 1928L.toBigInteger())


    val input = readInput("Day09")
    measureTime {
        part1(input).println()
        // 6446899523367
        // 1.458132800s
    }.println()

    check(part2(testInput).apply { println() } == 2858L.toBigInteger())
    measureTime {
        part2(input).println()
        // 6478232739671
        // 769.610100ms
    }.println()
}
