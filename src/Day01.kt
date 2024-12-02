import kotlin.math.abs

fun main() {
    fun numbersLists(input: List<String>): Pair<List<Int>, List<Int>> {
        val firsts = mutableListOf<Int>()
        val seconds = mutableListOf<Int>()
        input.forEach {
            val (first, second) = it.split(regex = Regex("\\s+"))
            firsts.add(first.toInt())
            seconds.add(second.toInt())
        }
        return Pair(firsts, seconds)
    }

    fun part1(input: List<String>): Int {
        val (firsts, seconds) = numbersLists(input)
        val sortedSeconds = seconds.sorted()
        return firsts.sorted().mapIndexed { index, i -> abs(sortedSeconds[index] - i)  }.sum()
    }

    fun part2(input: List<String>): Int {
        val (firsts, seconds) = numbersLists(input)
        return firsts.sumOf { value ->
            seconds.filter { it == value }.sum()
        }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
