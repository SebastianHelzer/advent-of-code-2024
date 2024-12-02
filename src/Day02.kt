fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { it.split(regex = Regex("\\s+")).map { it.toInt() } }
            .count { report -> report.isSafe() }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { it.split(regex = Regex("\\s+")).map { it.toInt() } }
            .count { report ->
                report.isSafe() || (0..report.lastIndex).any { report.toMutableList().apply { removeAt(it) }.isSafe() }
            }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

private fun List<Int>.isSafe(): Boolean {
    val diffs =  windowed(2) { (first, second) -> first - second }
    return diffs.all { it in 1..3 } || diffs.all { it in -3 .. -1 }
}
